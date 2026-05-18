package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
    List<ProductJpaEntity> findAllByDeletedAtIsNull();
    boolean existsBySkuAndDeletedAtIsNull(String sku);
}