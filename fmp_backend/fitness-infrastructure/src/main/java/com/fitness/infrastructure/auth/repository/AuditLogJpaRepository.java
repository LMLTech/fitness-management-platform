package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, Long> {
    List<AuditLogEntity> findTop50ByOrderByCreatedAtDesc();
}