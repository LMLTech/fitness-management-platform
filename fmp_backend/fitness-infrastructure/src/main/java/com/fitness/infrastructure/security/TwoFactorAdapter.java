package com.fitness.infrastructure.security;

import com.fitness.core.auth.port.out.ITwoFactorPort;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Component;

@Component // Đánh dấu đây là Bean để Spring quản lý
public class TwoFactorAdapter implements ITwoFactorPort {

    // Thư viện Google Authenticator dùng để tạo và xác thực OTP
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    @Override
    public String generateSecretKey() {

        // Tạo secret key cho user
        final GoogleAuthenticatorKey key = gAuth.createCredentials();

        // Trả về secret key
        return key.getKey();
    }

    @Override
    public String getQrCodeUrl(String secret, String email) {

        // Tạo URL QR Code để user scan bằng Google Authenticator
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(
                "FitnessApp", // tên ứng dụng
                email,        // tài khoản user
                new GoogleAuthenticatorKey.Builder(secret).build()
        );
    }

    @Override
    public boolean verifyCode(String secret, int code) {

        // Kiểm tra mã OTP user nhập có đúng không
        return gAuth.authorize(secret, code);
    }
}