package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.WorkoutPlan;
import java.util.List;
import java.util.UUID;

public interface IWorkoutPlanUseCase {
    WorkoutPlan createWorkoutPlan(UUID trainerId, WorkoutPlan workoutPlan);
    List<WorkoutPlan> getMemberPlans(UUID memberId);
    WorkoutPlan getPlanDetails(UUID planId);
}