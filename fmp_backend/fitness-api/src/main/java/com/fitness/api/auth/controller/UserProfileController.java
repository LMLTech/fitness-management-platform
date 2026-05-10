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
@RequestMapping("/v1/users/me")
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
    // 1. Chỉ Admin mới vào được
    @GetMapping("/test-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> testAdmin() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Xin chào Quản trị viên!"));
    }

    // 2. Chỉ Huấn luyện viên (Trainer) mới vào được
    @GetMapping("/test-trainer")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<ApiResponse<String>> testTrainer() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Xin chào Huấn luyện viên (PT)!"));
    }

    // 3. Chỉ Lễ tân mới vào được
    @GetMapping("/test-receptionist")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<ApiResponse<String>> testReceptionist() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Xin chào Lễ tân phòng tập!"));
    }

    // 4. Chỉ Hội viên chính thức mới vào được
    @GetMapping("/test-member")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ApiResponse<String>> testMember() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Xin chào Hội viên chính thức!"));
    }

    // 5. Test đa quyền: Admin HOẶC Lễ tân đều vào được
    @GetMapping("/test-staff")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<String>> testStaffOnly() {
        return ResponseEntity.ok(ApiResponse.success("SUCCESS", "Khu vực dành cho nhân viên vận hành."));
    }
}