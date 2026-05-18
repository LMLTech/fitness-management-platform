package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.AutomatedWebhookRequest;
import com.fitness.api.auth.dto.ManualConfirmationRequest;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.port.in.IPaymentConfirmationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments/confirmation")
@RequiredArgsConstructor
public class PaymentConfirmationController {

    private final IPaymentConfirmationUseCase confirmationUseCase;

    // API Hứng Webhook tự động từ bên thứ ba cấu hình mở công khai hoặc kiểm tra Token Header
    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse<Void>> handleBankWebhook(
            @RequestHeader("X-Webhook-Token") String webhookToken,
            @RequestBody AutomatedWebhookRequest request) {

        confirmationUseCase.confirmPaymentAutomatically(request.getDescription(), request.getGatewayReference(), webhookToken);

        // CHUẨN HÓA THÔNG BÁO WEBHOOK: Chuẩn tổng quát bao thầu cho cả kích hoạt Gói tập lẫn cập nhật Đơn hàng
        return ResponseEntity.ok(ApiResponse.success(null, "Xác nhận thanh toán tự động và cập nhật trạng thái hệ thống thành công!"));
    }

    // API Cho phép Lễ tân hoặc Admin duyệt đóng tiền mặt trực tiếp tại quầy phòng Gym
    @PostMapping("/manual")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Void>> handleManualConfirmation(@RequestBody ManualConfirmationRequest request) {

        confirmationUseCase.confirmPaymentManually(request.getPaymentId(), request.getReferenceNote());

        //  CHUẨN HÓA THÔNG BÁO TẠI QUẦY: Chuẩn nghiệp vụ đối soát kế toán cho cả 2 phân hệ mua hàng/mua gói
        return ResponseEntity.ok(ApiResponse.success(null, "Xác nhận hóa đơn và cập nhật trạng thái giao dịch thủ công thành công!"));
    }
}