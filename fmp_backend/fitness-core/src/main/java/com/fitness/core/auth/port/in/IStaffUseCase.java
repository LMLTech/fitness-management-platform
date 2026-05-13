package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Staff;
import com.fitness.core.auth.domain.Receptionist;
import java.util.UUID;

public interface IStaffUseCase {
    // Tạo nhân viên kèm ca làm việc nếu là lễ tân
    Staff createStaff(Staff staff, String shift, String password);
}