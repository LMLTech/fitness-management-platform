package com.fitness.core.auth.domain;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCampaign {
    private UUID id;
    private String title;
    private String subject;
    private String htmlContent;
    private String targetAudience; // VD: 'All_Members', 'Expired_Soon'
    private String status;         // 'Draft', 'Sending', 'Sent'
}