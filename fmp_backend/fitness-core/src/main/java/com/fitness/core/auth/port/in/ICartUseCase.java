package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Cart;
import java.util.UUID;

public interface ICartUseCase {
    Cart getOrCreateCart(UUID userId);
    Cart addItemToCart(UUID userId, UUID productId, UUID variantId, Integer quantity);
    Cart updateItemQuantity(UUID userId, UUID productId, UUID variantId, Integer quantity);
    Cart removeItemFromCart(UUID userId, UUID productId, UUID variantId);
}