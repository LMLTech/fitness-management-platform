package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.WorkoutPlanExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutPlanExerciseJpaRepository extends JpaRepository<WorkoutPlanExerciseEntity, UUID> {
    List<WorkoutPlanExerciseEntity> findByPlanId(UUID planId);
}