package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.*;
import com.fitness.core.auth.port.in.IStaffUseCase;
import com.fitness.core.auth.port.out.*;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffService implements IStaffUseCase {
    private final IStaffRepositoryPort staffRepositoryPort;
    private final IUserRepositoryPort userRepositoryPort;
    private final IPasswordEncoderPort passwordEncoderPort;

    @Override
    @Transactional
    public Staff createStaff(Staff staff, String shift, String password) {
        // 1. Kiểm tra mã nhân viên
        if (staffRepositoryPort.existsByEmployeeId(staff.getEmployeeId())) {
            throw new DomainException("STAFF_EXISTS", "Mã nhân viên " + staff.getEmployeeId() + " đã tồn tại!");
        }

        // 2. Chuẩn bị dữ liệu User
        User user = staff.getUser();
        user.setPasswordHash(passwordEncoderPort.encode(password));
        user.setStatus("Active");

        // Lưu User
        User savedUser = userRepositoryPort.saveUserOnly(user);

        // 3. Logic gán quyền thông minh
        String roleName;
        if (shift != null || (staff.getJobTitle() != null && staff.getJobTitle().equalsIgnoreCase("Receptionist"))) {
            roleName = "ROLE_RECEPTIONIST";
        } else if (staff.getJobTitle() != null && staff.getJobTitle().equalsIgnoreCase("Trainer")) {
            roleName = "ROLE_TRAINER";
        } else {
            roleName = "ROLE_STAFF";
        }

        // Gán quyền vào Database
        userRepositoryPort.assignRoleToUser(savedUser.getId(), roleName);

        // Roles vừa gán, createdAt
        User updatedUser = userRepositoryPort.findById(savedUser.getId())
                .orElseThrow(() -> new DomainException("SYNC_ERROR", "Không thể đồng bộ dữ liệu người dùng sau khi tạo"));

        // 4. Lưu hồ sơ Staff
        staff.setUserId(updatedUser.getId());
        staff.setUser(updatedUser);
        staffRepositoryPort.saveStaff(staff);

        // 5. Lưu hồ sơ Lễ tân nếu có
        if (roleName.equals("ROLE_RECEPTIONIST")) {
            String finalShift = (shift != null) ? shift : "Chưa phân ca";
            staffRepositoryPort.saveReceptionist(new Receptionist(updatedUser.getId(), finalShift));
        }

        return staff;
    }
}