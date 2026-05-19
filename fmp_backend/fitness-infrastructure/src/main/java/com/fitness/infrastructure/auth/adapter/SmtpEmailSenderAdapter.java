package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.port.out.IEmailSenderPort;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpEmailSenderAdapter implements IEmailSenderPort {

    // JavaMailSender dùng để gửi email qua SMTP
    private final JavaMailSender javaMailSender;

    @Override
    public void sendProfessionalHtmlEmail(String toAddress, String subject, String htmlBody) {
        try {
            // Tạo một email MIME mới để hỗ trợ HTML
            MimeMessage message = javaMailSender.createMimeMessage();

            // Helper hỗ trợ thiết lập nội dung email với UTF-8 để tránh lỗi tiếng Việt
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Thiết lập địa chỉ người nhận
            helper.setTo(toAddress);

            // Thiết lập tiêu đề email
            helper.setSubject(subject);

            // Thiết lập nội dung email dạng HTML
            helper.setText(htmlBody, true);

            // Tiến hành gửi email qua SMTP
            javaMailSender.send(message);

        } catch (Exception e) {
            // Bắt lỗi nếu quá trình gửi email thất bại
            throw new RuntimeException("Lỗi khi thực thi gửi mail qua SMTP: " + e.getMessage());
        }
    }
}