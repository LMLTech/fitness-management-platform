package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.LeaveRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
    // Repository quản lý bảng LeaveRequest
public interface LeaveRequestJpaRepository extends JpaRepository<LeaveRequestJpaEntity, Long> {
    // Lấy danh sách đơn nghỉ phép theo staffId
    List<LeaveRequestJpaEntity> findByStaffId(UUID staffId);
}