package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.ReceptionistJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ReceptionistJpaRepository extends JpaRepository<ReceptionistJpaEntity, UUID> {
    // Hiện tại chỉ cần các hàm CRUD cơ bản của JpaRepository là đủ
}