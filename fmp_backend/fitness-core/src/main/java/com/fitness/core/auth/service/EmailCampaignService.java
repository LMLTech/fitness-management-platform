package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.EmailCampaign;
import com.fitness.core.auth.port.in.IEmailCampaignUseCase;
import com.fitness.core.auth.port.out.IEmailCampaignRepositoryPort;
import com.fitness.core.auth.port.out.IEmailSenderPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailCampaignService implements IEmailCampaignUseCase {

    private final IEmailCampaignRepositoryPort campaignRepoPort;
    private final IEmailSenderPort emailSenderPort;

    @Override
    @Transactional
    public EmailCampaign createDraft(EmailCampaign campaign) {
        if (campaign.getSubject() == null || campaign.getSubject().trim().isEmpty()) {
            throw new DomainException("INVALID_SUBJECT", "Tiêu đề email không được để trống");
        }

        campaign.setId(UUID.randomUUID());
        campaign.setStatus("Draft");
        String professionalFitnessTemplate =
                "<!DOCTYPE html>" +
                        "<html lang='vi'>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "</head>" +
                        "<body style='margin: 0; padding: 0; background-color: #050505;'>" +
                        // Vùng chứa tổng: Nền radial hào quang tối padding dùng % để co giãn tốt trên điện thoại
                        "<div style='background-color: #050505; background-image: radial-gradient(circle at 50%% 0%%, #590000 0%%, #050505 80%%); padding: 50px 5%%; font-family: Arial, Helvetica, sans-serif;'>" +

                        // Khối thẻ trung tâm: Thêm width: 100% và box-sizing để chống tràn viền mobile
                        "<div style='max-width: 650px; width: 100%%; box-sizing: border-box; margin: 0 auto; background-color: #121212; border-radius: 16px; box-shadow: 0 0 35px rgba(255, 75, 43, 0.4), 0 0 80px rgba(255, 65, 108, 0.2); border: 1px solid #331111; overflow: hidden;'>" +

                        // Header Logo & Tên Thương Hiệu (Hardcode theo yêu cầu)
                        "<div style='background: #0a0a0a; padding: 35px 20px; text-align: center; border-bottom: 2px solid #ff4b2b;'>" +
                        "<h1 style='color: #ffffff; margin: 0; font-size: clamp(24px, 5vw, 30px); text-transform: uppercase; letter-spacing: 2px; text-shadow: 0 0 10px #ff4b2b, 0 0 20px #ff416c, 0 0 40px #ff416c;'>FITNESS MANAGEMENT PLATFORM</h1>" +
                        "<p style='color: #ffeadd; font-size: 13px; letter-spacing: 4px; margin-top: 12px; font-weight: bold; text-shadow: 0 0 8px rgba(255, 252, 221, 0.8);'>BỨT PHÁ MỌI GIỚI HẠN</p>" +
                        "</div>" +

                        // Banner Quảng Cáo Động: Ép khung chống méo ảnh
                        "<div style='background-color: #121212; width: 100%%;'>" +
                        "<img src='%s' style='width: 100%%; max-height: 450px; object-fit: cover; border-bottom: 3px solid #ff4b2b; display: block;' alt='Fitness Campaign'>" +
                        "</div>" +

                        // Nội dung chính: white-space giữ nguyên xuống dòng của Admin
                        "<div style='padding: 35px 6%%; background-color: #121212; box-sizing: border-box;'>" +
                        "<div style='color: #ffffff; font-size: 16px; line-height: 1.8; font-weight: 400; white-space: pre-line; text-align: justify;'>" +
                        "%s" +
                        "</div>" +

                        // Nút Call to Action
                        "<div style='text-align: center; margin-top: 45px; margin-bottom: 15px;'>" +
                        "<a href='#' style='display: inline-block; padding: 16px 40px; background: linear-gradient(90deg, #ff416c 0%%, #ff4b2b 100%%); color: #ffffff; text-decoration: none; font-size: 16px; font-weight: 900; border-radius: 50px; text-transform: uppercase; letter-spacing: 2px; box-shadow: 0 10px 30px rgba(255, 75, 43, 0.6); border: 1px solid #ff8a65;'>🔥 ĐĂNG KÝ NGAY 🔥</a>" +
                        "</div>" +
                        "</div>" +

                        // Footer chân trang
                        "<div style='background-color: #0a0a0a; padding: 25px 20px; text-align: center; border-top: 1px solid #222222; box-sizing: border-box;'>" +
                        "<p style='color: #ff4b2b; font-size: 15px; font-weight: 900; margin: 0 0 10px 0; letter-spacing: 1px;'>HỆ THỐNG FITNESS MANAGEMENT PLATFORM</p>" +
                        "<p style='color: #aaaaaa; font-size: 13px; line-height: 1.6; margin: 0;'>" +
                        "Hotline: 1900 9999 | Email: contact@fitnessplatform.vn<br>" +
                        "<span style='font-size: 11px; color: #555555;'>Đây là email tự động, vui lòng không phản hồi.</span>" +
                        "</p>" +
                        "</div>" +
                        "</div>" +
                        "<div style='text-align: center; margin-top: 35px; padding-bottom: 20px;'>" +
                        "<span style='font-size: 13px; font-weight: bold; letter-spacing: 4px; color: #ffffff; text-transform: uppercase; text-shadow: 0 -1px 4px #FFF, 0 -2px 10px #ffca28, 0 -10px 20px #ff4b2b, 0 -18px 40px #ff0000;'>LMLTECH</span>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

        // Gắn link ảnh và chữ vào template (Chú ý: phải giữ nguyên logic escape %%)
        String finalHtml = String.format(professionalFitnessTemplate, campaign.getImageUrl(), campaign.getContent());
        campaign.setHtmlContent(finalHtml);

        return campaignRepoPort.save(campaign);
    }

    @Override
    @Transactional
    public void sendCampaign(UUID campaignId) {
        EmailCampaign campaign = campaignRepoPort.findById(campaignId)
                .orElseThrow(() -> new DomainException("CAMPAIGN_NOT_FOUND", "Chiến dịch email không tồn tại"));

        if (!"Draft".equals(campaign.getStatus())) {
            throw new DomainException("INVALID_STATUS", "Chỉ có thể gửi chiến dịch đang ở trạng thái nháp (Draft)");
        }

        campaignRepoPort.updateStatus(campaignId, "Sending");

        List<String> targetEmails = campaignRepoPort.getTargetEmails(campaign.getTargetAudience());

        for (String email : targetEmails) {
            try {
                emailSenderPort.sendProfessionalHtmlEmail(email, campaign.getSubject(), campaign.getHtmlContent());
            } catch (Exception e) {
                System.err.println("Gặp sự cố khi phân phối mail tới hòm thư: " + email + " -> " + e.getMessage());
            }
        }

        campaignRepoPort.updateStatus(campaignId, "Sent");
    }
}