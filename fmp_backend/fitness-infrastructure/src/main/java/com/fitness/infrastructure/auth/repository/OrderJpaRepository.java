package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    List<OrderJpaEntity> findAllByUserIdAndDeletedAtIsNull(UUID userId);
}