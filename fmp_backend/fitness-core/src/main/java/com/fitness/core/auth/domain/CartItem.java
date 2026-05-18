package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CartItem {
    private UUID id;
    private UUID cartId;
    private UUID productId;
    private UUID variantId; // Có thể NULL nếu là sản phẩm không có biến thể
    private Integer quantity;
}