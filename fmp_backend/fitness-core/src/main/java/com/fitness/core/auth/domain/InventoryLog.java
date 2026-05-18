package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InventoryLog {
    private UUID id;
    private UUID branchId;
    private UUID productId;
    private UUID variantId; // Có thể NULL
    private String changeType; // 'IMPORT', 'EXPORT', 'ADJUST'
    private Integer quantityChange; // Số lượng thay đổi luôn là số dương
    private UUID createdBy; // ID của nhân viên thực hiện thao tác
    private LocalDateTime createdAt;
}