package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.InventoryLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface InventoryLogJpaRepository extends JpaRepository<InventoryLogJpaEntity, UUID> {
}