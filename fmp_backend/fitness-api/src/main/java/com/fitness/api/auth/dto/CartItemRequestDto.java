package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CartItemRequestDto {
    private UUID productId;
    private UUID variantId; // Nhận giá trị null nếu không có biến thể size/vị
    private Integer quantity;
}