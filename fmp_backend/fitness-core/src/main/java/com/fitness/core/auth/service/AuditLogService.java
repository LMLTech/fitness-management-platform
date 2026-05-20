package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.AuditLog;
import com.fitness.core.auth.port.in.IAuditLogUseCase;
import com.fitness.core.auth.port.out.IAuditLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogUseCase {

    private final IAuditLogRepositoryPort auditLogRepoPort;

    @Override
    public List<AuditLog> getRecentAuditLogs(int limit) {
        return auditLogRepoPort.findRecentLogs(limit);
    }
}