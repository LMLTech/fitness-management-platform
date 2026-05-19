package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface BadgeJpaRepository extends JpaRepository<BadgeEntity, UUID> {
}