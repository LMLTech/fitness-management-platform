package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.ChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ChallengeJpaRepository extends JpaRepository<ChallengeEntity, UUID> {}