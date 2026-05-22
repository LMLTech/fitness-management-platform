package com.fitness.core.auth.port.in;

public interface IForgotPasswordUseCase {
    // Bước 1: Khách hàng nhập Email, hệ thống sinh mã OTP lưu vào Redis và gửi qua Mail
    void requestPasswordReset(String email);

    // Bước 2: Khách hàng nhập OTP và Mật khẩu mới, hệ thống đối chiếu Redis và đổi Pass
    void verifyOtpAndResetPassword(String email, String otp, String newPassword);
}