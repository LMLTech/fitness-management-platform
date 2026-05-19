package com.fitness.core.auth.domain;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingLog {
    private UUID id;
    private UUID ptSessionId;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Double weight; // Mức tạ thực tế (kg)
    private String notes;  // Đánh giá thể lực từ HLV
}