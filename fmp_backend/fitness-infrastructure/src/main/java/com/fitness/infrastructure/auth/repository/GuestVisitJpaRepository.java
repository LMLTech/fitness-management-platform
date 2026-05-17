package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.GuestVisitJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GuestVisitJpaRepository extends JpaRepository<GuestVisitJpaEntity, UUID> {
}