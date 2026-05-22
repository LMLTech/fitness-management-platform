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
import com.fitness.core.auth.port.in.INotificationUseCase;
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
    private final INotificationUseCase notificationUseCase;

    @Value("${JWT_SECRET}")
    private String systemWebhookSecret;

    @Override
    @Transactional
    public void confirmPaymentAutomatically(String transferDescription, String gatewayTransactionCode, String secretWebhookToken) {
        if (!systemWebhookSecret.equals(secretWebhookToken)) {
            throw new DomainException("INVALID_WEBHOOK_TOKEN", "Chữ ký bảo mật mã Webhook không khớp");
        }

        if (transferDescription == null || !transferDescription.toUpperCase().contains("FMP")) {
            throw new DomainException("INVALID_TRANSACTION_CONTENT", "Nội dung chuyển khoản không khớp cấu trúc hệ thống phòng gym");
        }

        String cleanContent = transferDescription.toUpperCase().replaceAll("\\s+", "");
        int index = cleanContent.indexOf("FMP");
        if (index + 11 > cleanContent.length()) {
            throw new DomainException("INVALID_CODE_LENGTH", "Mã tham chiếu thanh toán không đủ độ dài tiêu chuẩn");
        }
        String uuidPrefix = cleanContent.substring(index + 3, index + 11);

        Payment payment = paymentRepoPort.findPendingPaymentByUuidPrefix(uuidPrefix)
                .orElseThrow(() -> new DomainException("PAYMENT_NOT_FOUND", "Không tìm thấy hóa đơn khớp với mã nội dung: " + uuidPrefix));

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

    private void processActivation(Payment payment, String transactionCode) {
        payment.setStatus("Success");
        payment.setTransactionCode(transactionCode);
        paymentRepoPort.save(payment);

        // Luồng A: Nếu tờ hóa đơn này đính kèm mã GÓI TẬP HỘI VIÊN
        if (payment.getSubscriptionId() != null) {
            Subscription subscription = subscriptionRepoPort.findById(payment.getSubscriptionId())
                    .orElseThrow(() -> new DomainException("SUBSCRIPTION_NOT_FOUND", "Không tìm thấy hợp đồng dịch vụ đi kèm"));

            MembershipPlan plan = membershipPlanRepoPort.findById(subscription.getPlanId())
                    .orElseThrow(() -> new DomainException("PLAN_NOT_FOUND", "Cấu hình gói tập không tồn tại trên hệ thống"));

            LocalDate activatedDate = LocalDate.now();
            subscription.setStartDate(activatedDate);
            subscription.setEndDate(activatedDate.plusMonths(plan.getDurationMonths()));
            subscription.setStatus("Active");

            subscriptionRepoPort.save(subscription);
            notificationUseCase.createNotification(
                    payment.getUserId(),
                    "Kích hoạt gói tập thành công 🎉",
                    "Cảm ơn bạn đã thanh toán! Gói tập [" + plan.getName() + "] của bạn đã được kích hoạt. Chúc bạn tập luyện hiệu quả!",
                    "PAYMENT_SUCCESS"
            );
        }

        // Luồng B: Nếu tờ hóa đơn này đính kèm mã ĐƠN HÀNG VẬT LÝ (Product Order)
        else if (payment.getOrderId() != null) {
            Order order = orderRepoPort.findOrderById(payment.getOrderId())
                    .orElseThrow(() -> new DomainException("ORDER_NOT_FOUND", "Không tìm thấy dữ liệu đơn hàng liên quan đến hóa đơn này"));

            order.setOrderStatus("COMPLETED");
            orderRepoPort.saveOrder(order);
            notificationUseCase.createNotification(
                    payment.getUserId(),
                    "Thanh toán đơn hàng thành công 🛒",
                    "Đơn hàng mã #" + order.getOrderNumber() + " của bạn đã được thanh toán xong. Vui lòng nhận hàng tại quầy Lễ tân.",
                    "ORDER_SUCCESS"
            );
        }

        else {
            throw new DomainException("INVALID_PAYMENT_RELATION", "Hóa đơn thanh toán hợp lệ nhưng không tìm thấy liên kết đến đơn hàng hoặc gói tập");
        }
    }
}