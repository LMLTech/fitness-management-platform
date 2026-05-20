package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.AuditLog;
import com.fitness.core.auth.port.in.IAuditLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final IAuditLogUseCase auditLogUseCase;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getSystemLogs(
            @RequestParam(defaultValue = "50") int limit) {

        List<AuditLog> logs = auditLogUseCase.getRecentAuditLogs(limit);
        return ResponseEntity.ok(ApiResponse.success(logs, "Lấy nhật ký hệ thống thành công"));
    }
}