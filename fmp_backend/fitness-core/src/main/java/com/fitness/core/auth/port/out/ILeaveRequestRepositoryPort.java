package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.LeaveRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Port kết nối Core với tầng Infrastructure để thao tác dữ liệu đơn nghỉ phép
public interface ILeaveRequestRepositoryPort {

    // Lưu hoặc cập nhật đơn nghỉ phép
    LeaveRequest save(LeaveRequest leaveRequest);

    // Tìm đơn nghỉ phép theo ID
    Optional<LeaveRequest> findById(Long id);

    // Lấy danh sách đơn nghỉ theo staffId
    List<LeaveRequest> findByStaffId(UUID staffId);

    // Lấy tất cả đơn nghỉ phép
    List<LeaveRequest> findAll();
}