package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.RoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, Long> {
    // Tìm role theo tên
    Optional<RoleJpaEntity> findByName(String name);
}