package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.BranchProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface BranchProductJpaRepository extends JpaRepository<BranchProductJpaEntity, UUID> {

    // Xử lý logic tìm kiếm tồn kho
    @Query("SELECT b FROM BranchProductJpaEntity b WHERE b.branchId = :branchId AND b.productId = :productId AND " +
            "((:variantId IS NULL AND b.variantId IS NULL) OR (b.variantId = :variantId))")
    Optional<BranchProductJpaEntity> findStock(
            @Param("branchId") UUID branchId,
            @Param("productId") UUID productId,
            @Param("variantId") UUID variantId
    );
}