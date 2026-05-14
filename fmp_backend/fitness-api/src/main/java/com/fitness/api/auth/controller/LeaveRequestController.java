package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.CreateLeaveRequestDto;
import com.fitness.api.auth.dto.ReviewLeaveRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.LeaveRequest;
import com.fitness.core.auth.port.in.ILeaveRequestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leaves")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final ILeaveRequestUseCase leaveRequestUseCase;


    //  API 1: Nhân viên tự tạo đơn nghỉ phép

    @PostMapping("/staff/{staffId}")
    public ResponseEntity<ApiResponse<LeaveRequest>> submitRequest(
            @PathVariable UUID staffId,
            @RequestBody CreateLeaveRequestDto dto) {

        LeaveRequest request = LeaveRequest.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .build();

        LeaveRequest created = leaveRequestUseCase.createRequest(staffId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Gửi đơn xin nghỉ phép thành công, vui lòng chờ duyệt!"));
    }


    //  API 2: Nhân viên xem lịch sử nghỉ phép cá nhân

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<ApiResponse<List<LeaveRequest>>> getStaffHistory(@PathVariable UUID staffId) {
        List<LeaveRequest> history = leaveRequestUseCase.getRequestsByStaff(staffId);
        return ResponseEntity.ok(ApiResponse.success(history, "Lấy danh sách đơn nghỉ phép thành công!"));
    }


    //  API 3: Admin duyệt hoặc từ chối đơn nghỉ phép

    @PutMapping("/{id}/review")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<LeaveRequest>> reviewRequest(
            @PathVariable Long id,
            @RequestBody ReviewLeaveRequestDto dto) {

        LeaveRequest reviewed = leaveRequestUseCase.reviewRequest(id, dto.getStatus());
        return ResponseEntity.ok(ApiResponse.success(reviewed, "Đã cập nhật trạng thái đơn nghỉ phép!"));
    }


    //  API 4: Admin xem toàn bộ danh sách đơn nghỉ phép trong hệ thống

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<LeaveRequest>>> getAllRequests() {
        List<LeaveRequest> allRequests = leaveRequestUseCase.getAllRequests();
        return ResponseEntity.ok(ApiResponse.success(allRequests, "Lấy danh sách đơn nghỉ phép hệ thống thành công!"));
    }
}