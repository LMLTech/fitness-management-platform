package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.StaffJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface StaffJpaRepository extends JpaRepository<StaffJpaEntity, UUID> {
    // Hàm này để kiểm tra xem mã nhân viên (NV001...) đã tồn tại chưa
    boolean existsByEmployeeId(String employeeId);
}