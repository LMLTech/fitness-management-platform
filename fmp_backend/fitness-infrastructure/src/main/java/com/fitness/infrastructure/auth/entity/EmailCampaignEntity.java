package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "email_campaigns")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCampaignEntity {
    @Id
    private UUID id;

    @Column(length = 150)
    private String title;

    private String subject;

    @Column(name = "html_content", columnDefinition = "LONGTEXT")
    private String htmlContent;

    @Column(name = "target_audience", length = 50)
    private String targetAudience;

    @Column(length = 20)
    private String status;
}