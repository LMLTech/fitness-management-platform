package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.AddressJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AddressJpaRepository extends JpaRepository<AddressJpaEntity, UUID> {
    List<AddressJpaEntity> findByUserId(UUID userId);
}