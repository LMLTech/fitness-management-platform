package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.MembershipPlanRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.MembershipPlan;
import com.fitness.core.auth.domain.PlanPermission;
import com.fitness.core.auth.port.in.IMembershipPlanUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class MembershipPlanController {

    // Gọi UseCase xử lý nghiệp vụ gói tập
    private final IMembershipPlanUseCase useCase;

    // TẠO GÓI TẬP MỚI (chỉ ADMIN)
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<MembershipPlan>> create(
            @RequestBody MembershipPlanRequestDto dto
    ) {

        // Convert danh sách quyền từ DTO → Domain
        List<PlanPermission> domainPermissions = dto.getPermissions().stream()
                .map(p -> PlanPermission.builder()
                        .permissionKey(p.getPermissionKey().toUpperCase())
                        .permissionValue(p.getPermissionValue())
                        .build())
                .collect(Collectors.toList());

        // Convert DTO → Domain MembershipPlan
        MembershipPlan model = MembershipPlan.builder()
                .name(dto.getName())
                .basePrice(dto.getBasePrice())
                .durationMonths(dto.getDurationMonths())
                .planType(dto.getPlanType().toUpperCase())
                .maxSessionsPerMonth(dto.getMaxSessionsPerMonth())
                .permissions(domainPermissions)
                .build();

        // Gọi service tạo mới
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        useCase.createPlan(model),
                        "Tạo cấu hình gói tập thương mại thành công!"
                ));
    }

    // CẬP NHẬT GÓI TẬP (chỉ ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<MembershipPlan>> update(
            @PathVariable UUID id,
            @RequestBody MembershipPlanRequestDto dto
    ) {

        // Convert quyền từ DTO → Domain
        List<PlanPermission> domainPermissions = dto.getPermissions().stream()
                .map(p -> PlanPermission.builder()
                        .permissionKey(p.getPermissionKey().toUpperCase())
                        .permissionValue(p.getPermissionValue())
                        .build())
                .collect(Collectors.toList());

        // Convert DTO → Domain MembershipPlan
        MembershipPlan model = MembershipPlan.builder()
                .name(dto.getName())
                .basePrice(dto.getBasePrice())
                .durationMonths(dto.getDurationMonths())
                .planType(dto.getPlanType().toUpperCase())
                .maxSessionsPerMonth(dto.getMaxSessionsPerMonth())
                .permissions(domainPermissions)
                .build();

        // Gọi service cập nhật
        return ResponseEntity.ok(
                ApiResponse.success(
                        useCase.updatePlan(id, model),
                        "Cập nhật gói tập thành công"
                )
        );
    }

    // LẤY CHI TIẾT GÓI TẬP THEO ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MembershipPlan>> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        useCase.getPlanById(id),
                        "Lấy chi tiết gói tập thành công"
                )
        );
    }

    // LẤY TOÀN BỘ DANH SÁCH GÓI TẬP
    @GetMapping
    public ResponseEntity<ApiResponse<List<MembershipPlan>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        useCase.getAllPlans(),
                        "Lấy danh mục gói tập hệ thống thành công"
                )
        );
    }

    // XÓA MỀM GÓI TẬP (chỉ ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id
    ) {
        useCase.deletePlan(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Xóa gói tập khỏi danh mục thương mại thành công"
                )
        );
    }
}