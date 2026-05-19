package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.PersonalTrainingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PersonalTrainingSessionJpaRepository extends JpaRepository<PersonalTrainingSessionEntity, UUID> {}