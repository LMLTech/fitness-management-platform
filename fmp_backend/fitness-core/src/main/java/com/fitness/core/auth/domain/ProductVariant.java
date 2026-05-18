package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductVariant {
    private UUID id;
    private UUID productId;
    private String sku;
    private String attributes;
    private BigDecimal priceAdjustment;
    private LocalDateTime deletedAt;
}