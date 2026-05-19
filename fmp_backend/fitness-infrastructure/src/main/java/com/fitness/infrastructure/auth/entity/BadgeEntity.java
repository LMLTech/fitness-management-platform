package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "badges")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeEntity {
    @Id
    private UUID id;
    private String name;
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}