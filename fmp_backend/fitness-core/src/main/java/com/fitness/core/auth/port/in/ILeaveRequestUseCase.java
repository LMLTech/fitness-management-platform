package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.LeaveRequest;
import com.fitness.core.auth.domain.LeaveStatus;
import java.util.List;
import java.util.UUID;

// Input Port Use Case định nghĩa các chức năng xử lý đơn xin nghỉ phép
public interface ILeaveRequestUseCase {

    // Nhân viên tạo đơn xin nghỉ phép mới
    LeaveRequest createRequest(UUID staffId, LeaveRequest request);

    // Admin/Manager duyệt hoặc từ chối đơn nghỉ phép
    LeaveRequest reviewRequest(Long id, LeaveStatus status);

    // Lấy danh sách đơn nghỉ phép của một nhân viên theo staffId
    List<LeaveRequest> getRequestsByStaff(UUID staffId);

    // Lấy toàn bộ danh sách đơn nghỉ phép
    List<LeaveRequest> getAllRequests();
}