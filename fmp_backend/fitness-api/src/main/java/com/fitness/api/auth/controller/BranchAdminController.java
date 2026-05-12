package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Branch;
import com.fitness.core.auth.port.in.IBranchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/branches")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // Bảo mật Flow 5 đã làm
public class BranchAdminController {

    private final IBranchUseCase branchUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<Branch>> create(@RequestBody Branch branch) {
        return ResponseEntity.ok(ApiResponse.success(branchUseCase.createBranch(branch), "Tạo chi nhánh thành công"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Branch>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(branchUseCase.getAllBranches()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Branch>> update(@PathVariable UUID id, @RequestBody Branch branch) {
        return ResponseEntity.ok(ApiResponse.success(branchUseCase.updateBranch(id, branch), "Cập nhật thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        branchUseCase.deleteBranch(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã xóa chi nhánh"));
    }
}