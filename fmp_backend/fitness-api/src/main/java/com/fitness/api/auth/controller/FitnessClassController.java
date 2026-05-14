package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.FitnessClassDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.FitnessClass;
import com.fitness.core.auth.port.in.IFitnessClassUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController // Đánh dấu đây là REST Controller xử lý API
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor // Tự động inject constructor
public class FitnessClassController {

    // Gọi UseCase xử lý logic nghiệp vụ môn học
    private final IFitnessClassUseCase useCase;

    // TẠO MÔN HỌC MỚI chỉ ADMIN
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<FitnessClass>> create(@RequestBody FitnessClassDto dto) {

        // Convert DTO → Domain object
        FitnessClass fitnessClass = FitnessClass.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .classType(dto.getClassType().toUpperCase()) // Chuẩn hóa chữ hoa
                .difficulty(dto.getDifficulty().toUpperCase()) // Chuẩn hóa chữ hoa
                .defaultMaxCapacity(dto.getDefaultMaxCapacity())
                .build();

        // Gọi service tạo mới
        FitnessClass created = useCase.createClass(fitnessClass);

        // Trả về HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Định nghĩa môn học mới thành công!"));
    }

    // CẬP NHẬT THÔNG TIN MÔN HỌC (chỉ ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<FitnessClass>> update(
            @PathVariable UUID id,
            @RequestBody FitnessClassDto dto
    ) {
        // Convert DTO → Domain object
        FitnessClass fitnessClass = FitnessClass.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .classType(dto.getClassType().toUpperCase())
                .difficulty(dto.getDifficulty().toUpperCase())
                .defaultMaxCapacity(dto.getDefaultMaxCapacity())
                .build();

        // Gọi service cập nhật
        FitnessClass updated = useCase.updateClass(id, fitnessClass);

        return ResponseEntity.ok(
                ApiResponse.success(updated, "Cập nhật danh mục môn học thành công")
        );
    }

    // LẤY CHI TIẾT 1 MÔN HỌC THEO ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FitnessClass>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        useCase.getClassById(id),
                        "Lấy chi tiết môn học thành công"
                )
        );
    }

    // LẤY TOÀN BỘ DANH SÁCH MÔN HỌC
    @GetMapping
    public ResponseEntity<ApiResponse<List<FitnessClass>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        useCase.getAllClasses(),
                        "Lấy danh mục môn học hệ thống thành công"
                )
        );
    }

    // XÓA MỀM MÔN HỌC chỉ ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        // Gọi service xóa mềm
        useCase.deleteClass(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Xóa môn học khỏi danh mục thành công")
        );
    }
}