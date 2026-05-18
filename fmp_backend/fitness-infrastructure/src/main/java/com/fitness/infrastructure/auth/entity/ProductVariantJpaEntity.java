package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(columnDefinition = "JSON")
    private String attributes;

    @Column(name = "price_adjustment", nullable = false, precision = 12, scale = 2)
    private BigDecimal priceAdjustment;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}