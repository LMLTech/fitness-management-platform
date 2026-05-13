package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Staff;
import com.fitness.core.auth.domain.Receptionist;

public interface IStaffRepositoryPort {
    void saveStaff(Staff staff);
    void saveReceptionist(Receptionist receptionist);
    boolean existsByEmployeeId(String employeeId);
}