package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.TrainingLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingLogJpaRepository extends JpaRepository<TrainingLogEntity, UUID> {
    List<TrainingLogEntity> findByPtSessionId(UUID ptSessionId);
}