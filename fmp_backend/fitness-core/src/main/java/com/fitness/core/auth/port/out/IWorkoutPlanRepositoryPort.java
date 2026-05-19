package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.WorkoutPlan;
import com.fitness.core.auth.domain.WorkoutPlanExercise;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IWorkoutPlanRepositoryPort {
    WorkoutPlan savePlan(WorkoutPlan plan);
    void saveExercises(List<WorkoutPlanExercise> exercises);
    List<WorkoutPlan> findByMemberId(UUID memberId);
    Optional<WorkoutPlan> findById(UUID planId);
    List<WorkoutPlanExercise> findExercisesByPlanId(UUID planId);
}