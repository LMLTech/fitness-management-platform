package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Cart;
import com.fitness.core.auth.domain.CartItem;
import com.fitness.core.auth.port.out.ICartRepositoryPort;
import com.fitness.infrastructure.auth.entity.CartJpaEntity;
import com.fitness.infrastructure.auth.entity.CartItemJpaEntity;
import com.fitness.infrastructure.auth.repository.CartJpaRepository;
import com.fitness.infrastructure.auth.repository.CartItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartMysqlAdapter implements ICartRepositoryPort {

    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public Cart saveCart(Cart cart) {
        CartJpaEntity entity = CartJpaEntity.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .build();
        CartJpaEntity saved = cartJpaRepository.save(entity);
        cart.setId(saved.getId());
        return cart;
    }

    @Override
    public Optional<Cart> findCartByUserId(UUID userId) {
        return cartJpaRepository.findByUserId(userId)
                .map(entity -> Cart.builder()
                        .id(entity.getId())
                        .userId(entity.getUserId())
                        .build());
    }

    @Override
    public Optional<CartItem> findItemInCart(UUID cartId, UUID productId, UUID variantId) {
        return cartItemJpaRepository.findItem(cartId, productId, variantId)
                .map(entity -> CartItem.builder()
                        .id(entity.getId())
                        .cartId(entity.getCartId())
                        .productId(entity.getProductId())
                        .variantId(entity.getVariantId())
                        .quantity(entity.getQuantity())
                        .build());
    }

    @Override
    public CartItem saveItem(CartItem cartItem) {
        CartItemJpaEntity entity = CartItemJpaEntity.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCartId())
                .productId(cartItem.getProductId())
                .variantId(cartItem.getVariantId())
                .quantity(cartItem.getQuantity())
                .build();
        CartItemJpaEntity saved = cartItemJpaRepository.save(entity);
        cartItem.setId(saved.getId());
        return cartItem;
    }

    @Override
    public List<CartItem> findItemsByCartId(UUID cartId) {
        return cartItemJpaRepository.findAllByCartId(cartId).stream()
                .map(entity -> CartItem.builder()
                        .id(entity.getId())
                        .cartId(entity.getCartId())
                        .productId(entity.getProductId())
                        .variantId(entity.getVariantId())
                        .quantity(entity.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(UUID cartItemId) {
        cartItemJpaRepository.deleteById(cartItemId);
    }
}