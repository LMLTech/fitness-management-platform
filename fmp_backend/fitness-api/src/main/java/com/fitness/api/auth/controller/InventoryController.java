package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.StockAdjustmentRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.BranchProduct;
import com.fitness.core.auth.port.in.IInventoryUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final IInventoryUseCase inventoryUseCase;
    private final IUserRepositoryPort userRepoPort;

    @PostMapping("/adjust")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<BranchProduct>> adjustStock(@RequestBody StockAdjustmentRequestDto dto) {
        // Lấy thông tin tài khoản nhân viên đang trực tiếp thao tác nhập xuất kho
        String currentStaffEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID staffId = userRepoPort.findByEmail(currentStaffEmail)
                .map(u -> u.getId())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Không tìm thấy thông tin tài khoản nhân viên"));

        BranchProduct updatedStock = inventoryUseCase.adjustStock(
                dto.getBranchId(),
                dto.getProductId(),
                dto.getVariantId(),
                dto.getChangeType(),
                dto.getQuantity(),
                staffId
        );

        return ResponseEntity.ok(ApiResponse.success(updatedStock, "Cập nhật dữ liệu điều phối tồn kho chi nhánh thành công!"));
    }
}