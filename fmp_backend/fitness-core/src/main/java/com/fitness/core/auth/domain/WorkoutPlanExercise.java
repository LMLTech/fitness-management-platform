package com.fitness.core.auth.domain;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlanExercise {
    private UUID id;
    private UUID planId;
    private String exerciseName;
    private Integer targetSets;
    private Integer targetReps;
    private String dayOfWeek; // MONDAY, TUESDAY...
}