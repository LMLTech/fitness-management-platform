package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.WorkoutPlan;
import com.fitness.core.auth.domain.WorkoutPlanExercise;
import com.fitness.core.auth.port.out.IWorkoutPlanRepositoryPort;
import com.fitness.infrastructure.auth.entity.WorkoutPlanEntity;
import com.fitness.infrastructure.auth.entity.WorkoutPlanExerciseEntity;
import com.fitness.infrastructure.auth.repository.WorkoutPlanExerciseJpaRepository;
import com.fitness.infrastructure.auth.repository.WorkoutPlanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkoutPlanMysqlAdapter implements IWorkoutPlanRepositoryPort {

    private final WorkoutPlanJpaRepository planJpaRepo;
    private final WorkoutPlanExerciseJpaRepository exerciseJpaRepo;

    @Override
    public WorkoutPlan savePlan(WorkoutPlan plan) {
        WorkoutPlanEntity entity = WorkoutPlanEntity.builder()
                .id(plan.getId())
                .trainerId(plan.getTrainerId())
                .memberId(plan.getMemberId())
                .title(plan.getTitle())
                .description(plan.getDescription())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .createdAt(plan.getCreatedAt())
                .build();

        planJpaRepo.save(entity);
        return plan;
    }

    @Override
    public void saveExercises(List<WorkoutPlanExercise> exercises) {
        if (exercises == null) return;
        List<WorkoutPlanExerciseEntity> entities = exercises.stream()
                .map(ex -> WorkoutPlanExerciseEntity.builder()
                        .id(ex.getId())
                        .planId(ex.getPlanId())
                        .exerciseName(ex.getExerciseName())
                        .targetSets(ex.getTargetSets())
                        .targetReps(ex.getTargetReps())
                        .dayOfWeek(ex.getDayOfWeek())
                        .build())
                .collect(Collectors.toList());

        exerciseJpaRepo.saveAll(entities);
    }

    @Override
    public List<WorkoutPlan> findByMemberId(UUID memberId) {
        return planJpaRepo.findByMemberIdAndDeletedAtIsNull(memberId).stream()
                .map(entity -> WorkoutPlan.builder()
                        .id(entity.getId())
                        .trainerId(entity.getTrainerId())
                        .memberId(entity.getMemberId())
                        .title(entity.getTitle())
                        .description(entity.getDescription())
                        .startDate(entity.getStartDate())
                        .endDate(entity.getEndDate())
                        .createdAt(entity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WorkoutPlan> findById(UUID planId) {
        return planJpaRepo.findById(planId)
                .map(entity -> WorkoutPlan.builder()
                        .id(entity.getId())
                        .trainerId(entity.getTrainerId())
                        .memberId(entity.getMemberId())
                        .title(entity.getTitle())
                        .description(entity.getDescription())
                        .startDate(entity.getStartDate())
                        .endDate(entity.getEndDate())
                        .createdAt(entity.getCreatedAt())
                        .build());
    }

    @Override
    public List<WorkoutPlanExercise> findExercisesByPlanId(UUID planId) {
        return exerciseJpaRepo.findByPlanId(planId).stream()
                .map(entity -> WorkoutPlanExercise.builder()
                        .id(entity.getId())
                        .planId(entity.getPlanId())
                        .exerciseName(entity.getExerciseName())
                        .targetSets(entity.getTargetSets())
                        .targetReps(entity.getTargetReps())
                        .dayOfWeek(entity.getDayOfWeek())
                        .build())
                .collect(Collectors.toList());
    }
}