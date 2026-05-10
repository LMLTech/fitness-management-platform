package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.PermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, Long> {
    // Tìm quyền theo tên nếu cần
    java.util.Optional<PermissionJpaEntity> findByName(String name);
}