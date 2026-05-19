package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.MemberPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MemberPointJpaRepository extends JpaRepository<MemberPointEntity, UUID> {}