package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.MembershipPlan;
import com.fitness.core.auth.domain.Payment;
import com.fitness.core.auth.domain.Order;
import com.fitness.core.auth.port.out.IOrderRepositoryPort;
import com.fitness.core.auth.domain.Subscription;
import com.fitness.core.auth.port.in.IPaymentConfirmationUseCase;
import com.fitness.core.auth.port.out.IMembershipPlanRepositoryPort;
import com.fitness.core.auth.port.out.IPaymentRepositoryPort;
import com.fitness.core.auth.port.out.ISubscriptionRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentConfirmationService implements IPaymentConfirmationUseCase {

    private final IPaymentRepositoryPort paymentRepoPort;
    private final ISubscriptionRepositoryPort subscriptionRepoPort;
    private final IMembershipPlanRepositoryPort membershipPlanRepoPort;
    private final IOrderRepositoryPort orderRepoPort;

    @Value("${JWT_SECRET}") // Sử dụng chuỗi bảo mật hệ thống để verify webhook chống tấn công giả mạo
    private String systemWebhookSecret;

    @Override
    @Transactional
    public void confirmPaymentAutomatically(String transferDescription, String gatewayTransactionCode, String secretWebhookToken) {
        // 1. Kiểm tra mã bảo mật webhook ngăn chặn hacker gửi request ảo
        if (!systemWebhookSecret.equals(secretWebhookToken)) {
            throw new DomainException("INVALID_WEBHOOK_TOKEN", "Chữ ký bảo mật mã Webhook không khớp");
        }

        // 2. Phân tích bóc tách nội dung chuyển khoản để lấy 8 ký tự mã hóa đơn (VD: "FMPB5F8E21A" --> "B5F8E21A")
        if (transferDescription == null || !transferDescription.toUpperCase().contains("FMP")) {
            throw new DomainException("INVALID_TRANSACTION_CONTENT", "Nội dung chuyển khoản không khớp cấu trúc hệ thống phòng gym");
        }

        String cleanContent = transferDescription.toUpperCase().replaceAll("\\s+", "");
        int index = cleanContent.indexOf("FMP");
        if (index + 11 > cleanContent.length()) {
            throw new DomainException("INVALID_CODE_LENGTH", "Mã tham chiếu thanh toán không đủ độ dài tiêu chuẩn");
        }
        String uuidPrefix = cleanContent.substring(index + 3, index + 11);

        // 3. Truy vấn tìm hóa đơn tương ứng trong cơ sở dữ liệu
        Payment payment = paymentRepoPort.findPendingPaymentByUuidPrefix(uuidPrefix)
                .orElseThrow(() -> new DomainException("PAYMENT_NOT_FOUND", "Không tìm thấy hóa đơn khớp với mã nội dung: " + uuidPrefix));

        // 4. Thực thi kích hoạt kích hoạt dòng tiền
        processActivation(payment, gatewayTransactionCode);
    }

    @Override
    @Transactional
    public void confirmPaymentManually(UUID paymentId, String referenceCode) {
        Payment payment = paymentRepoPort.findById(paymentId)
                .orElseThrow(() -> new DomainException("PAYMENT_NOT_FOUND", "Hóa đơn xử lý thủ công không tồn tại"));

        if (!"Pending".equalsIgnoreCase(payment.getStatus())) {
            throw new DomainException("INVALID_PAYMENT_STATUS", "Hóa đơn này đã được xử lý hoặc đã hủy trước đó");
        }

        processActivation(payment, "MANUAL_CONFIRM_" + referenceCode.toUpperCase());
    }

    // HÀM NÂNG CẤP PHÂN CHIA VAI TRÒ DÒNG TIỀN: RÕ RÀNG - MINH BẠCH - KHÔNG XUNG ĐỘT
    private void processActivation(Payment payment, String transactionCode) {
        // 1. Cập nhật trạng thái hóa đơn thanh toán thành công chung cho cả hệ thống
        payment.setStatus("Success");
        payment.setTransactionCode(transactionCode);
        paymentRepoPort.save(payment);

        // 2. RẼ NHÁNH NGHIỆP VỤ: MUA GÓI RA MUA GÓI, MUA HÀNG RA MUA HÀNG

        // Luồng A: Nếu tờ hóa đơn này đính kèm mã GÓI TẬP HỘI VIÊN (Subscription)
        if (payment.getSubscriptionId() != null) {
            // Truy vấn hợp đồng dịch vụ đính kèm
            Subscription subscription = subscriptionRepoPort.findById(payment.getSubscriptionId())
                    .orElseThrow(() -> new DomainException("SUBSCRIPTION_NOT_FOUND", "Không tìm thấy hợp đồng dịch vụ đi kèm"));

            // Truy vấn gói tập thương mại để lấy số tháng hiệu lực cấu hình gốc
            MembershipPlan plan = membershipPlanRepoPort.findById(subscription.getPlanId())
                    .orElseThrow(() -> new DomainException("PLAN_NOT_FOUND", "Cấu hình gói tập không tồn tại trên hệ thống"));

            // Thiết lập kích hoạt mốc thời gian thực tế từ ngày đóng tiền thành công
            LocalDate activatedDate = LocalDate.now();
            subscription.setStartDate(activatedDate);
            subscription.setEndDate(activatedDate.plusMonths(plan.getDurationMonths()));
            subscription.setStatus("Active");

            subscriptionRepoPort.save(subscription);
        }

        // Luồng B: Nếu tờ hóa đơn này đính kèm mã ĐƠN HÀNG VẬT LÝ (Product Order từ Giỏ Hàng)
        else if (payment.getOrderId() != null) {
            // Truy vấn tờ đơn hàng mua Whey/Nước uống gốc lên
            Order order = orderRepoPort.findOrderById(payment.getOrderId())
                    .orElseThrow(() -> new DomainException("ORDER_NOT_FOUND", "Không tìm thấy dữ liệu đơn hàng liên quan đến hóa đơn này"));

            // Cập nhật trạng thái đơn hàng trực tuyến từ PENDING thành COMPLETED (Hoàn tất)
            order.setOrderStatus("COMPLETED");

            // Lưu trạng thái đơn hàng mới cập nhật xuống cơ sở dữ liệu thật
            orderRepoPort.saveOrder(order);
        }

        // Luồng C: Trường hợp phòng hờ nếu hóa đơn lỗi hệ thống thiếu cả 2 liên kết
        else {
            throw new DomainException("INVALID_PAYMENT_RELATION", "Hóa đơn thanh toán hợp lệ nhưng không tìm thấy liên kết đến đơn hàng hoặc gói tập");
        }
    }
}