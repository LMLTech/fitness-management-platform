package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Product {
    private UUID id;
    private UUID categoryId;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal costPrice;
    private String imageUrls;
    private LocalDateTime deletedAt;
    private List<ProductVariant> variants;
}