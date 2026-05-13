package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Address;
import com.fitness.core.auth.domain.User;
import com.fitness.core.auth.port.in.IProfileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserProfileController {
    private final IProfileUseCase profileUseCase;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getMyProfile(@RequestParam UUID userId) {
        User user = profileUseCase.getProfile(userId);
        user.setPasswordHash(null);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<User>> updateProfile(
            @RequestParam UUID userId,
            @RequestBody User updateRequest) {
        User updated = profileUseCase.updateProfile(userId, updateRequest.getFullName(),
                updateRequest.getGender(), updateRequest.getAvatarUrl());
        updated.setPasswordHash(null);
        return ResponseEntity.ok(ApiResponse.success(updated, "Cập nhật hồ sơ thành công"));
    }

    @PostMapping("/addresses")
    public ResponseEntity<ApiResponse<Address>> addAddress(@RequestBody Address address) {
        return ResponseEntity.ok(ApiResponse.success(profileUseCase.addAddress(address), "Thêm địa chỉ thành công"));
    }

    @GetMapping("/addresses")
    public ResponseEntity<ApiResponse<List<Address>>> getAddresses(@RequestParam UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(profileUseCase.getUserAddresses(userId)));
    }

    // CÁC HÀM TEST PHÂN QUYỀN
    // 1. Chỉ Admin
    @GetMapping("/test-admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> testAdmin() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Chào Quản trị viên!"));
    }

    // 2. Chỉ PT
    @GetMapping("/test-trainer")
    @PreAuthorize("hasAuthority('ROLE_TRAINER')")
    public ResponseEntity<ApiResponse<String>> testTrainer() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Chào Huấn luyện viên!"));
    }

    // 3. Chỉ Lễ tân
    @GetMapping("/test-receptionist")
    @PreAuthorize("hasAuthority('ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<String>> testReceptionist() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Chào Lễ tân!"));
    }

    // 4. Chỉ Hội viên
    @GetMapping("/test-member")
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public ResponseEntity<ApiResponse<String>> testMember() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Chào Hội viên!"));
    }

    // 5. Khu vực nhân viên (Admin, Lễ tân, PT, Staff đều vào được)
    @GetMapping("/test-staff")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST', 'ROLE_TRAINER', 'ROLE_STAFF')")
    public ResponseEntity<ApiResponse<String>> testStaffOnly() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Khu vực dành cho nhân viên vận hành."));
    }
}