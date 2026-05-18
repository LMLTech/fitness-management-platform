package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.CartItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemJpaRepository extends JpaRepository<CartItemJpaEntity, UUID> {

    List<CartItemJpaEntity> findAllByCartId(UUID cartId);

    //  Tìm kiếm item trong giỏ, chấp nhận variantId mang giá trị NULL
    @Query("SELECT c FROM CartItemJpaEntity c WHERE c.cartId = :cartId AND c.productId = :productId AND " +
            "((:variantId IS NULL AND c.variantId IS NULL) OR (c.variantId = :variantId))")
    Optional<CartItemJpaEntity> findItem(
            @Param("cartId") UUID cartId,
            @Param("productId") UUID productId,
            @Param("variantId") UUID variantId
    );
}