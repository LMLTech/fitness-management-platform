package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    private Long id;
    private String userId; // UUID dạng chuỗi của Admin thực hiện thao tác
    private String action; // CREATE, UPDATE, DELETE
    private String entityType; // Tên bảng/đối tượng (VD: "SupportTicket", "MembershipPlan")
    private String entityId; // ID của bản ghi bị tác động
    private String oldValues; // Chuỗi JSON
    private String newValues; // Chuỗi JSON
    private LocalDateTime createdAt;
}