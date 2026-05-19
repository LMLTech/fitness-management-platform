package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trainer_reviews")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerReviewEntity {
    @Id
    private UUID id;
    @Column(name = "trainer_id")
    private UUID trainerId;
    @Column(name = "reviewer_id")
    private UUID reviewerId;
    private Integer rating;
    @Column(columnDefinition = "TEXT")
    private String comment;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}