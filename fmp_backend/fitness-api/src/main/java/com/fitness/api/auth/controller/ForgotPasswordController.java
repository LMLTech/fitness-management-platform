package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.port.in.IForgotPasswordUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final IForgotPasswordUseCase forgotPasswordUseCase;

    // API 1: Yêu cầu gửi mã OTP
    @PostMapping("/forgot")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        forgotPasswordUseCase.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(null, "Mã xác thực OTP đã được gửi đến email của bạn (Hiệu lực 15 phút)."));
    }

    // API 2: Xác nhận OTP và đặt lại mật khẩu mới
    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody ResetPasswordRequest request) {
        forgotPasswordUseCase.verifyOtpAndResetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );
        return ResponseEntity.ok(ApiResponse.success(null, "Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại."));
    }

    // --- CÁC CLASS DTO NHẬN DỮ LIỆU ---
    @Data
    public static class ForgotPasswordRequest {
        private String email;
    }

    @Data
    public static class ResetPasswordRequest {
        private String email;
        private String otp;
        private String newPassword;
    }
}