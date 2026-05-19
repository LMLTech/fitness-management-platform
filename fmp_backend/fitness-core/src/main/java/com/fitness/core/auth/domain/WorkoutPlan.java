package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlan {
    private UUID id;
    private UUID trainerId;
    private UUID memberId;
    private String title;
    private String description; // Mô tả mục tiêu: Giảm cân, tăng cơ...
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private List<WorkoutPlanExercise> exercises; // Danh sách bài tập đi kèm
}