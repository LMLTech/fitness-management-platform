package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.TrainerReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TrainerReviewJpaRepository extends JpaRepository<TrainerReviewEntity, UUID> {
}