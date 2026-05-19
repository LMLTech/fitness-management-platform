package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmailCampaignDto {
    private String title;
    private String subject;
    private String htmlContent;
    private String targetAudience;
}