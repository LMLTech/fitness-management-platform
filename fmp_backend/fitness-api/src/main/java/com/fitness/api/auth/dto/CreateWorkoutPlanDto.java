package com.fitness.api.auth.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWorkoutPlanDto {
    private UUID memberId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ExerciseDto> exercises;

    @Getter
    @Setter
    public static class ExerciseDto {
        private String exerciseName;
        private Integer targetSets;
        private Integer targetReps;
        private String dayOfWeek; // MONDAY, TUESDAY...
    }
}