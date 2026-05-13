package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Staff;
import com.fitness.core.auth.domain.Receptionist;
import com.fitness.core.auth.port.out.IStaffRepositoryPort;
import com.fitness.infrastructure.auth.entity.StaffJpaEntity;
import com.fitness.infrastructure.auth.entity.ReceptionistJpaEntity;
import com.fitness.infrastructure.auth.repository.StaffJpaRepository;
import com.fitness.infrastructure.auth.repository.ReceptionistJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaffMysqlAdapter implements IStaffRepositoryPort {
    private final StaffJpaRepository staffRepo;
    private final ReceptionistJpaRepository receptionistRepo;

    @Override
    public void saveStaff(Staff staff) {
        staffRepo.save(StaffJpaEntity.builder()
                .userId(staff.getUserId())
                .branchId(staff.getBranchId())
                .employeeId(staff.getEmployeeId())
                .jobTitle(staff.getJobTitle())
                .build());
    }

    @Override
    public void saveReceptionist(Receptionist receptionist) {
        receptionistRepo.save(ReceptionistJpaEntity.builder()
                .userId(receptionist.getUserId())
                .shift(receptionist.getShift())
                .build());
    }

    @Override
    public boolean existsByEmployeeId(String employeeId) {
        return staffRepo.existsByEmployeeId(employeeId);
    }
}