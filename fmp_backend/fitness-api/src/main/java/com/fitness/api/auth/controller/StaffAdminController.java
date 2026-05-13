package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Staff;
import com.fitness.core.auth.port.in.IStaffUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/staffs")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class StaffAdminController {

    private final IStaffUseCase staffUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<Staff>> createStaff(
            @RequestBody Staff staffRequest,
            @RequestParam(required = false) String shift) {

        // 1. Thực hiện nghiệp vụ tạo nhân viên mật khẩu mặc định sau này đổi sau
        Staff created = staffUseCase.createStaff(staffRequest, shift, "staff123");

        // 2. bảo mật API
        if (created != null && created.getUser() != null) {
            // Đảm bảo ID bên trong đối tượng User đồng bộ với userId của Staff
            created.getUser().setId(created.getUserId());

            // Tuyệt đối không bao giờ trả về mã băm mật khẩu
            created.getUser().setPasswordHash(null);

            // Xóa bớt các thông tin null không cần thiết để JSON trả về gọn sạch
            created.getUser().setTwoFactorSecret(null);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Hệ thống đã tạo tài khoản nhân viên thành công!"));
    }
}