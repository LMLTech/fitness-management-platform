package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.ProductVariantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductVariantJpaRepository extends JpaRepository<ProductVariantJpaEntity, UUID> {
    boolean existsBySkuAndDeletedAtIsNull(String sku);
}