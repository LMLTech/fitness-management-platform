package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.AuditLog;
import java.util.List;

public interface IAuditLogUseCase {
    List<AuditLog> getRecentAuditLogs(int limit);
}