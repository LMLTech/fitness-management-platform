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
        return ResponseEntity.ok(ApiResponse.success(null, "Hệ thống tự động kích hoạt hợp đồng gói tập thành công!"));
    }

    // API Cho phép Lễ tân hoặc Admin duyệt đóng tiền mặt trực tiếp tại quầy phòng Gym
    @PostMapping("/manual")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Void>> handleManualConfirmation(@RequestBody ManualConfirmationRequest request) {

        confirmationUseCase.confirmPaymentManually(request.getPaymentId(), request.getReferenceNote());
        return ResponseEntity.ok(ApiResponse.success(null, "Xác nhận hóa đơn và kích hoạt gói tập thủ công thành công!"));
    }
}