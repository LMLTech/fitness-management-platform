package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FitnessClassDto {

    // Tên lớp học
    private String name;

    // Mô tả chi tiết lớp học
    private String description;

    // Loại lớp học (VD: YOGA, CARDIO, DANCE)
    private String classType;

    // Mức độ khó (VD: BEGINNER, INTERMEDIATE, ADVANCED)
    private String difficulty;

    // Sức chứa tối đa mặc định của lớp
    private Integer defaultMaxCapacity;
}