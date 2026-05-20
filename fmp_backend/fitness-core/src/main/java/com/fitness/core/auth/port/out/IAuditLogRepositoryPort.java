package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.AuditLog;
import java.util.List;

public interface IAuditLogRepositoryPort {
    void saveLog(AuditLog log);
    List<AuditLog> findRecentLogs(int limit);
}