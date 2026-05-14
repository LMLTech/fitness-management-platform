package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.RoomJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, UUID> {
    List<RoomJpaEntity> findByBranchId(UUID branchId);
}