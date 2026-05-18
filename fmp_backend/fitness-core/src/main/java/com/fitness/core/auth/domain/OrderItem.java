package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderItem {
    private UUID id;
    private UUID orderId;
    private UUID productId;
    private UUID variantId; // Có thể NULL
    private Integer quantity;
    private BigDecimal unitPrice;
}