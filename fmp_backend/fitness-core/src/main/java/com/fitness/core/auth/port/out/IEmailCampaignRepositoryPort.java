package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.EmailCampaign;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEmailCampaignRepositoryPort {
    EmailCampaign save(EmailCampaign campaign);
    Optional<EmailCampaign> findById(UUID id);
    void updateStatus(UUID id, String status);
    List<String> getTargetEmails(String targetAudience);
}