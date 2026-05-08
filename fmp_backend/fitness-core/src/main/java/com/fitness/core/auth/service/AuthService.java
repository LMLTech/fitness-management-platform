package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.AuthResult;
import com.fitness.core.auth.domain.Member;
import com.fitness.core.auth.domain.User;
import com.fitness.core.auth.port.in.IAuthUseCase;
import com.fitness.core.auth.port.in.LoginCommand;
import com.fitness.core.auth.port.in.RegisterCommand;
import com.fitness.core.auth.port.out.IJwtTokenPort;
import com.fitness.core.auth.port.out.IPasswordEncoderPort;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import org.springframework.stereotype.Service;
import com.fitness.core.auth.port.out.ITwoFactorPort;
import java.util.Map;

import java.util.UUID;

@Service
public class AuthService implements IAuthUseCase {

    private final IUserRepositoryPort userRepositoryPort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final IJwtTokenPort jwtTokenPort;
    private final ITwoFactorPort twoFactorPort;

    public AuthService(IUserRepositoryPort userRepositoryPort,
                       IPasswordEncoderPort passwordEncoderPort,
                       IJwtTokenPort jwtTokenPort,
                       ITwoFactorPort twoFactorPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.jwtTokenPort = jwtTokenPort;
        this.twoFactorPort = twoFactorPort;
    }

    @Override
    public AuthResult register(RegisterCommand command) {
        if (userRepositoryPort.existsByEmail(command.getEmail())) {
            throw new DomainException("EMAIL_EXISTS", "Email này đã được đăng ký.");
        }
        if (userRepositoryPort.existsByUsername(command.getUsername())) {
            throw new DomainException("USERNAME_EXISTS", "Tên đăng nhập này đã có người sử dụng.");
        }
        if (userRepositoryPort.existsByPhoneNumber(command.getPhoneNumber())) {
            throw new DomainException("PHONE_EXISTS", "Số điện thoại này đã tồn tại.");
        }

        User newUser = User.builder()
                .username(command.getUsername())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .fullName(command.getFullName())
                .passwordHash(passwordEncoderPort.encode(command.getRawPassword()))
                .status("Active")
                .is2faEnabled(false)
                .build();

        String generatedMemberCode = "MEM-" + (System.currentTimeMillis() / 1000);
        String generatedReferralCode = "REF-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Member newMember = Member.builder()
                .memberCode(generatedMemberCode)
                .referralCode(generatedReferralCode)
                .build();

        // Lưu và nhận về User đã có ID và createdAt từ DB
        User savedUser = userRepositoryPort.saveUserAndMember(newUser, newMember);

        return AuthResult.builder()
                .user(savedUser)
                .member(newMember)
                .build();
    }

    @Override
    public String login(LoginCommand command) {
        User user = userRepositoryPort.findByEmail(command.getEmail())
                .orElseThrow(() -> new DomainException("INVALID_CREDENTIALS", "Email hoặc mật khẩu không đúng."));

        if (!passwordEncoderPort.matches(command.getRawPassword(), user.getPasswordHash())) {
            throw new DomainException("INVALID_CREDENTIALS", "Email hoặc mật khẩu không đúng.");
        }

        if ("Banned".equals(user.getStatus())) {
            throw new DomainException("USER_BANNED", "Tài khoản của bạn đã bị khóa.");
        }

        return jwtTokenPort.generateToken(user);
    }

    @Override
    public Map<String, String> setup2FA(UUID userId) {

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() ->
                        new DomainException("USER_NOT_FOUND", "Người dùng không tồn tại"));

        // Tạo secret key cho Google Authenticator
        String secret = twoFactorPort.generateSecretKey();

        // Lưu secret xuống database
        userRepositoryPort.update2FASecret(userId, secret);

        // Tạo QR Code URL
        String qrCodeUrl = twoFactorPort.getQrCodeUrl(secret, user.getEmail());

        return Map.of(
                "secret", secret,
                "qrCodeUrl", qrCodeUrl
        );
    }

    @Override
    public void enable2FA(UUID userId, int otpCode) {

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() ->
                        new DomainException("USER_NOT_FOUND", "Người dùng không tồn tại"));

        // chưa setup secret key
        if (user.getTwoFactorSecret() == null) {
            throw new DomainException(
                    "2FA_NOT_SETUP",
                    "Bạn chưa thiết lập mã 2FA"
            );
        }

        // kiểm tra mã OTP
        boolean isCodeValid =
                twoFactorPort.verifyCode(user.getTwoFactorSecret(), otpCode);

        if (isCodeValid) {

            // bật 2FA
            userRepositoryPort.enable2FA(userId);

        } else {

            throw new DomainException(
                    "INVALID_OTP",
                    "Mã xác thực không đúng hoặc đã hết hạn"
            );
        }
    }
}