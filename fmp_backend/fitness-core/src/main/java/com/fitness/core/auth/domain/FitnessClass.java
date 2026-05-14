package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FitnessClass {
    private UUID id;
    private String name;               // Tên môn học (VD: Yoga Ấn Độ, Boxing Cardio)
    private String description;        // Mô tả chi tiết môn học
    private String classType;          // Thể loại (VD: YOGA, CARDIO, DANCE, MARTIAL_ARTS)
    private String difficulty;         // Độ khó (VD: BEGINNER, INTERMEDIATE, ADVANCED)
    private Integer defaultMaxCapacity; // Sức chứa tối đa mặc định của lớp học
    private LocalDateTime deletedAt;
}