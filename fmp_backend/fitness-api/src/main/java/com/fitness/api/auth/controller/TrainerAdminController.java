package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.TrainerFullResponseDto;
import com.fitness.api.auth.dto.TrainerUpdateDto; // Import DTO đã tách riêng
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.TrainerProfile;
import com.fitness.core.auth.domain.User;
import com.fitness.core.auth.port.in.ITrainerUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import com.fitness.core.auth.port.out.IStaffRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/trainers")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class TrainerAdminController {

    private final ITrainerUseCase trainerUseCase;
    private final IUserRepositoryPort userRepositoryPort;
    private final IStaffRepositoryPort staffRepositoryPort;

    /**
     * API 1: Cập nhật thông tin chuyên sâu cho PT (Bio, Bằng cấp, Chuyên môn)
     * Thường dùng sau khi đã tạo User/Staff ở Flow 7
     */
    @PutMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse<TrainerProfile>> updateProfile(
            @PathVariable UUID userId,
            @RequestBody TrainerUpdateDto request) {

        // Chuyển đổi từ DTO sang Domain Object
        TrainerProfile profile = TrainerProfile.builder()
                .bio(request.getBio())
                .certifications(request.getCertifications())
                .commissionRate(request.getCommissionRate())
                .build();

        TrainerProfile updated = trainerUseCase.updateTrainerProfile(userId, profile, request.getSpecialties());

        return ResponseEntity.ok(ApiResponse.success(updated, "Cập nhật hồ sơ HLV thành công!"));
    }

    /**
     * API 2: Lấy hồ sơ đầy đủ của HLV (Dữ liệu tổng hợp từ nhiều bảng)
     * API này cực kỳ quan trọng để hiển thị cho Admin quản lý hoặc Hội viên xem PT
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<TrainerFullResponseDto>> getTrainerDetail(@PathVariable UUID userId) {

        // 1. Lấy thông tin tài khoản cơ bản
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tài khoản người dùng"));

        // 2. Lấy thông tin nhân sự (Mã nhân viên, chi nhánh)
        var staff = staffRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ nhân sự của người dùng này"));

        // 3. Lấy thông tin chuyên môn PT
        TrainerProfile profile = trainerUseCase.getTrainerProfile(userId);

        // 4. "Lắp ghép" dữ liệu vào DTO để trả về cho Frontend
        TrainerFullResponseDto response = TrainerFullResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .employeeId(staff.getEmployeeId())
                .jobTitle(staff.getJobTitle())
                .bio(profile.getBio())
                .certifications(profile.getCertifications())
                .commissionRate(profile.getCommissionRate())
                .specialties(profile.getSpecialties().stream()
                        .map(s -> s.getName()) // Chỉ lấy tên chuyên môn để JSON gọn sạch
                        .collect(Collectors.toSet()))
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "Lấy thông tin chi tiết HLV thành công!"));
    }
}