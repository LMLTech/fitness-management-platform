package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.TrainerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TrainerJpaRepository extends JpaRepository<TrainerJpaEntity, UUID> {
}