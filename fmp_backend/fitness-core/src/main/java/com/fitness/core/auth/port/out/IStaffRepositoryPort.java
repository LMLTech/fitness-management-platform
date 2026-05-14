package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Staff;
import com.fitness.core.auth.domain.Receptionist;
import java.util.Optional;
import java.util.UUID;

public interface IStaffRepositoryPort {
    void saveStaff(Staff staff);
    void saveReceptionist(Receptionist receptionist);
    boolean existsByEmployeeId(String employeeId);

    // Để tìm hồ sơ nhân sự theo ID người dùng
    Optional<Staff> findByUserId(UUID userId);
}