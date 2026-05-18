package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class StockAdjustmentRequestDto {
    private UUID branchId;
    private UUID productId;
    private UUID variantId; // Có thể để null nếu sản phẩm gốc không có chia variant
    private String changeType; // IMPORT, EXPORT, ADJUST
    private Integer quantity;
}