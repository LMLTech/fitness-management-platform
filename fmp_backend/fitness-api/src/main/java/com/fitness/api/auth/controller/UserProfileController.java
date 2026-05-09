package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Address;
import com.fitness.core.auth.domain.User;
import com.fitness.core.auth.port.in.IProfileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}