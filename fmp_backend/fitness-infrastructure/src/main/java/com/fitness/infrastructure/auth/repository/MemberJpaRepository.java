package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.MemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, UUID> {
}