package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.FitnessClassJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
// Tự động có sẵn CRUD: save(), findById(), findAll(), delete()...
public interface FitnessClassJpaRepository extends JpaRepository<FitnessClassJpaEntity, UUID> {
    // Tìm lớp học theo tên
    Optional<FitnessClassJpaEntity> findByName(String name);
    // Kiểm tra tên lớp đã tồn tại chưa (true/false)
    boolean existsByName(String name);
}