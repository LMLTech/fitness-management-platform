package com.fitness.core.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.core.auth.domain.AuditLog;
import com.fitness.core.auth.domain.AuditTrace;
import com.fitness.core.auth.port.out.IAuditLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final IAuditLogRepositoryPort auditLogRepoPort;
    private final ObjectMapper objectMapper;

    @AfterReturning(pointcut = "@annotation(auditTrace)", returning = "result")
    public void logAuditActivity(JoinPoint joinPoint, AuditTrace auditTrace, Object result) {
        try {
            String currentUserId = null;
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
            }

            Object[] args = joinPoint.getArgs();
            String newValuesJson = args.length > 0 ? objectMapper.writeValueAsString(args[0]) : "{}";

            String entityId = "N/A";
            if (result != null) {
                try {
                    entityId = result.getClass().getMethod("getId").invoke(result).toString();
                } catch (Exception e) {
                    entityId = "UNKNOWN";
                }
            }

            AuditLog auditLog = AuditLog.builder()
                    .userId(currentUserId)
                    .action(auditTrace.action())
                    .entityType(auditTrace.entityType())
                    .entityId(entityId)
                    .oldValues("{}")
                    .newValues(newValuesJson)
                    .createdAt(LocalDateTime.now())
                    .build();

            auditLogRepoPort.saveLog(auditLog);

        } catch (Exception e) {
            System.err.println("Lỗi trích xuất Audit Log hệ thống: " + e.getMessage());
        }
    }
}