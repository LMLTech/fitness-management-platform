package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.LeaveRequest;
import com.fitness.core.auth.port.out.ILeaveRequestRepositoryPort;
import com.fitness.infrastructure.auth.entity.LeaveRequestJpaEntity;
import com.fitness.infrastructure.auth.repository.LeaveRequestJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component // Adapter kết nối Core với database MySQL
@RequiredArgsConstructor // Tự inject repository qua constructor
public class LeaveRequestMysqlAdapter implements ILeaveRequestRepositoryPort {

    private final LeaveRequestJpaRepository leaveRequestJpaRepository;

    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        // Map Domain -> JPA Entity để lưu DB
        LeaveRequestJpaEntity entity = LeaveRequestJpaEntity.builder()
                .id(leaveRequest.getId())
                .staffId(leaveRequest.getStaffId())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .reason(leaveRequest.getReason())
                .status(leaveRequest.getStatus())
                .build();

        // Lưu xuống database
        LeaveRequestJpaEntity saved = leaveRequestJpaRepository.save(entity);

        // Map Entity -> Domain trả về Core
        return mapToDomain(saved);
    }

    @Override
    public Optional<LeaveRequest> findById(Long id) {
        // Tìm đơn nghỉ phép theo ID
        return leaveRequestJpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<LeaveRequest> findByStaffId(UUID staffId) {
        // Lấy tất cả đơn nghỉ của một nhân viên
        return leaveRequestJpaRepository.findByStaffId(staffId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequest> findAll() {
        // Lấy toàn bộ danh sách đơn nghỉ phép
        return leaveRequestJpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    // Hàm chuyển JPA Entity -> Domain
    private LeaveRequest mapToDomain(LeaveRequestJpaEntity entity) {
        return LeaveRequest.builder()
                .id(entity.getId())
                .staffId(entity.getStaffId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .reason(entity.getReason())
                .status(entity.getStatus())
                .build();
    }
}