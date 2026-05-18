package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.CartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartJpaRepository extends JpaRepository<CartJpaEntity, UUID> {
    Optional<CartJpaEntity> findByUserId(UUID userId);
}