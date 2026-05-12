package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.BranchJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
// extends JpaRepository<BranchJpaEntity, UUID>
public interface BranchJpaRepository extends JpaRepository<BranchJpaEntity, UUID> {

    @org.springframework.data.jpa.repository.Query("SELECT b FROM BranchJpaEntity b WHERE b.deletedAt IS NULL")
    List<BranchJpaEntity> findAllActive();

    Optional<BranchJpaEntity> findByCode(String code);
}