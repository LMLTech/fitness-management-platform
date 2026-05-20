package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.AuditLog;
import com.fitness.core.auth.port.out.IAuditLogRepositoryPort;
import com.fitness.infrastructure.auth.entity.AuditLogEntity;
import com.fitness.infrastructure.auth.repository.AuditLogJpaRepository;
import com.fitness.infrastructure.auth.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuditLogMysqlAdapter implements IAuditLogRepositoryPort {

    private final AuditLogJpaRepository repo;
    private final UserJpaRepository userRepo;

    @Override
    public void saveLog(AuditLog log) {
        UUID finalUserId = null;

        if (log.getUserId() != null && !log.getUserId().equals("anonymousUser")) {
            try {
                // 1. Thử parse thẳng xem có phải UUID không
                finalUserId = UUID.fromString(log.getUserId());
            } catch (IllegalArgumentException e) {
                // 2. Nếu parse lỗi (tức là nó là Email), thì tìm User theo Email để lấy ID
                finalUserId = userRepo.findByEmail(log.getUserId())
                        .map(user -> user.getId())
                        .orElse(null);
            }
        }

        AuditLogEntity entity = AuditLogEntity.builder()
                .userId(finalUserId)
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .oldValues(log.getOldValues())
                .newValues(log.getNewValues())
                .createdAt(log.getCreatedAt())
                .build();
        repo.save(entity);
    }

    @Override
    public List<AuditLog> findRecentLogs(int limit) {
        return repo.findTop50ByOrderByCreatedAtDesc().stream().map(entity -> AuditLog.builder()
                .id(entity.getId())
                .userId(entity.getUserId() != null ? entity.getUserId().toString() : null)
                .action(entity.getAction())
                .entityType(entity.getEntityType())
                .entityId(entity.getEntityId())
                .oldValues(entity.getOldValues())
                .newValues(entity.getNewValues())
                .createdAt(entity.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }
}