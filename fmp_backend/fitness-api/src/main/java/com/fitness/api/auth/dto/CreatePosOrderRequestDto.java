package com.fitness.api.auth.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePosOrderRequestDto {
    private UUID branchId;
    private UUID customerUserId;
    private List<PosItemDto> items;

    @Getter
    @Setter
    public static class PosItemDto {
        private UUID productId;
        private UUID variantId;
        private Integer quantity;
    }
}