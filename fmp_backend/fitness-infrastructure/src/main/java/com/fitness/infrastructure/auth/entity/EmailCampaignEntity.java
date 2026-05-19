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

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "html_content", columnDefinition = "LONGTEXT")
    private String htmlContent; // Đây vẫn là nơi lưu cục HTML hoàn chỉnh sau khi ghép ảnh + chữ

    @Column(name = "target_audience", length = 50)
    private String targetAudience;

    @Column(length = 20)
    private String status;
}