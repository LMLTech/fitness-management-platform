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
            throw new DomainException("INVALID_SUBJECT", "Tieu de email khong duoc de trong");
        }
        campaign.setId(UUID.randomUUID());
        campaign.setStatus("Draft");
        return campaignRepoPort.save(campaign);
    }

    @Override
    @Transactional
    public void sendCampaign(UUID campaignId) {
        EmailCampaign campaign = campaignRepoPort.findById(campaignId)
                .orElseThrow(() -> new DomainException("CAMPAIGN_NOT_FOUND", "Chien dich email khong ton tai"));

        if (!"Draft".equals(campaign.getStatus())) {
            throw new DomainException("INVALID_STATUS", "Chi co the gui chien dich dang o trang thai nhat (Draft)");
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