package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.OrderItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface OrderItemJpaRepository extends JpaRepository<OrderItemJpaEntity, UUID> {
    List<OrderItemJpaEntity> findAllByOrderIdAndDeletedAtIsNull(UUID orderId);
}