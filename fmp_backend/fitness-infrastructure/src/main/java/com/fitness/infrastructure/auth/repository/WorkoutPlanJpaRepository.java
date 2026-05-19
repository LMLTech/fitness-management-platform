package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.WorkoutPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutPlanJpaRepository extends JpaRepository<WorkoutPlanEntity, UUID> {
    List<WorkoutPlanEntity> findByMemberIdAndDeletedAtIsNull(UUID memberId);
}