package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerReview {
    private UUID id;
    private UUID trainerId;
    private UUID reviewerId;
    private Integer rating; // Số sao từ 1 đến 5
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}