package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.LeaveRequest;
import com.fitness.core.auth.domain.LeaveStatus;
import com.fitness.core.auth.port.in.ILeaveRequestUseCase;
import com.fitness.core.auth.port.out.ILeaveRequestRepositoryPort;
import com.fitness.core.auth.port.out.IStaffRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveRequestService implements ILeaveRequestUseCase {

    private final ILeaveRequestRepositoryPort leaveRepositoryPort;
    private final IStaffRepositoryPort staffRepositoryPort;

    @Override
    @Transactional
    public LeaveRequest createRequest(UUID staffId, LeaveRequest request) {
        // 1. Kiểm tra xem người dùng có phải nhân viên không
        staffRepositoryPort.findByUserId(staffId)
                .orElseThrow(() -> new DomainException("STAFF_NOT_FOUND", "Hồ sơ nhân viên không tồn tại"));

        // 2. Kiểm tra logic ngày nghỉ hợp lệ
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new DomainException("INVALID_DATE", "Ngày bắt đầu nghỉ không được ở quá khứ");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new DomainException("INVALID_DATE_RANGE", "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc");
        }

        // 3. Khởi tạo đơn ở trạng thái Chờ duyệt (Pending)
        request.setStaffId(staffId);
        request.setStatus(LeaveStatus.PENDING);

        return leaveRepositoryPort.save(request);
    }

    @Override
    @Transactional
    public LeaveRequest reviewRequest(Long id, LeaveStatus status) {
        LeaveRequest existingRequest = leaveRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("REQUEST_NOT_FOUND", "Không tìm thấy đơn nghỉ phép này"));

        if (existingRequest.getStatus() != LeaveStatus.PENDING) {
            throw new DomainException("INVALID_ACTION", "Đơn nghỉ phép này đã được xử lý từ trước");
        }

        existingRequest.setStatus(status);
        return leaveRepositoryPort.save(existingRequest);
    }

    @Override
    public List<LeaveRequest> getRequestsByStaff(UUID staffId) {
        return leaveRepositoryPort.findByStaffId(staffId);
    }

    @Override
    public List<LeaveRequest> getAllRequests() {
        return leaveRepositoryPort.findAll();
    }
}