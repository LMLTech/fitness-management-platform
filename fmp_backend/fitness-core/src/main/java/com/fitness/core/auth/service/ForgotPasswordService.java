package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.User;
import com.fitness.core.auth.port.in.IForgotPasswordUseCase;
import com.fitness.core.auth.port.out.ITokenStoragePort;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import com.fitness.core.auth.port.out.IEmailSenderPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService implements IForgotPasswordUseCase {

    private final IUserRepositoryPort userRepoPort;
    private final ITokenStoragePort tokenStoragePort;
    private final PasswordEncoder passwordEncoder;
    private final IEmailSenderPort emailSenderPort;

    private static final long OTP_EXPIRATION_MINUTES = 15;
    private static final String REDIS_KEY_PREFIX = "pwd_reset:";

    @Override
    public void requestPasswordReset(String email) {
        User user = userRepoPort.findByEmail(email)
                .orElseThrow(() -> new DomainException("USER_NOT_FOUND", "Không tìm thấy tài khoản với email này."));

        String otp = String.format("%06d", new Random().nextInt(999999));

        String redisKey = REDIS_KEY_PREFIX + email;
        tokenStoragePort.saveToken(redisKey, otp, OTP_EXPIRATION_MINUTES);

        //  giao diện Email dùng HTML để phù hợp với hàm sendProfessionalHtmlEmail
        String subject = "Mã xác thực OTP Đặt lại mật khẩu - LMLTech Fitness";
        String htmlBody = "<h3>Xin chào " + user.getFullName() + ",</h3>"
                + "<p>Bạn vừa yêu cầu đặt lại mật khẩu cho tài khoản. Mã xác thực OTP của bạn là: <strong style='font-size: 24px; color: #D32F2F;'>" + otp + "</strong></p>"
                + "<p>Mã này có hiệu lực trong vòng 15 phút. Vui lòng <b>KHÔNG CHIA SẺ</b> mã này cho bất kỳ ai để bảo vệ tài khoản.</p>"
                + "<br><p>Trân trọng,<br><b>Đội ngũ LMLTech Fitness</b></p>";

        // Gọi đúng tên hàm có trong file IEmailSenderPort của bạn
        emailSenderPort.sendProfessionalHtmlEmail(email, subject, htmlBody);
    }

    @Override
    @Transactional
    public void verifyOtpAndResetPassword(String email, String otp, String newPassword) {
        String redisKey = REDIS_KEY_PREFIX + email;

        String savedOtp = tokenStoragePort.getToken(redisKey)
                .orElseThrow(() -> new DomainException("OTP_EXPIRED", "Mã OTP đã hết hạn hoặc bạn chưa yêu cầu đặt lại mật khẩu."));

        if (!savedOtp.equals(otp)) {
            throw new DomainException("INVALID_OTP", "Mã xác thực OTP không chính xác!");
        }

        User user = userRepoPort.findByEmail(email)
                .orElseThrow(() -> new DomainException("USER_NOT_FOUND", "Tài khoản không tồn tại."));

        String encodedPassword = passwordEncoder.encode(newPassword);
        userRepoPort.updatePassword(user.getId(), encodedPassword);

        tokenStoragePort.deleteToken(redisKey);
    }
}