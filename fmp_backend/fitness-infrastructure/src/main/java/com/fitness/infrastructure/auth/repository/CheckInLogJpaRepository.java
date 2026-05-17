package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.CheckInLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CheckInLogJpaRepository extends JpaRepository<CheckInLogJpaEntity, UUID> {
}