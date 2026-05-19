package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.EmailCampaign;
import java.util.UUID;

public interface IEmailCampaignUseCase {
    EmailCampaign createDraft(EmailCampaign campaign);
    void sendCampaign(UUID campaignId);
}