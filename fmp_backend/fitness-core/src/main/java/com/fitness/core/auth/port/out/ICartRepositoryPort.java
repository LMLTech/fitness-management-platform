package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Cart;
import com.fitness.core.auth.domain.CartItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICartRepositoryPort {
    Cart saveCart(Cart cart);
    Optional<Cart> findCartByUserId(UUID userId);

    Optional<CartItem> findItemInCart(UUID cartId, UUID productId, UUID variantId);
    CartItem saveItem(CartItem cartItem);
    List<CartItem> findItemsByCartId(UUID cartId);
    void deleteItemById(UUID cartItemId);
}