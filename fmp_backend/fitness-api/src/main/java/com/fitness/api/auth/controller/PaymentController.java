package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Payment;
import com.fitness.core.auth.port.out.IPaymentProvider;
import com.fitness.core.auth.port.out.IPaymentRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final List<IPaymentProvider> paymentProviders;
    private final IPaymentRepositoryPort paymentRepositoryPort;

    @GetMapping("/{paymentId}/checkout")
    public ResponseEntity<ApiResponse<String>> getCheckoutDetails(@PathVariable UUID paymentId) {
        // 1. Tìm thông tin bản ghi hóa đơn thanh toán
        Payment payment = paymentRepositoryPort.findById(paymentId)
                .orElseThrow(() -> new DomainException("PAYMENT_NOT_FOUND", "Yêu cầu hóa đơn thanh toán không tồn tại trên hệ thống"));

        // 2. Duyệt tìm lớp Adapter phù hợp để xử lý phương thức thanh toán đã lựa chọn
        IPaymentProvider routingProvider = paymentProviders.stream()
                .filter(provider -> provider.supports(payment.getPaymentMethod()))
                .findFirst()
                .orElseThrow(() -> new DomainException("METHOD_NOT_SUPPORTED",
                        "Hệ thống hiện tại chưa hỗ trợ hình thức xử lý cho phương thức: " + payment.getPaymentMethod()));

        // 3. Thực thi lấy kết quả dữ liệu trả về tương ứng cho Client
        String checkoutData = routingProvider.getPaymentInstructions(paymentId, payment.getAmount());

        return ResponseEntity.ok(ApiResponse.success(checkoutData, "Khởi tạo dữ liệu liên kết thanh toán thành công!"));
    }
}