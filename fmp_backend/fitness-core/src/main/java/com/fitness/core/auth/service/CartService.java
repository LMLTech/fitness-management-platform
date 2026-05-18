package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Cart;
import com.fitness.core.auth.domain.CartItem;
import com.fitness.core.auth.port.in.ICartUseCase;
import com.fitness.core.auth.port.out.ICartRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService implements ICartUseCase {

    private final ICartRepositoryPort cartRepoPort;

    @Override
    @Transactional
    public Cart getOrCreateCart(UUID userId) {
        Cart cart = cartRepoPort.findCartByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .build();
                    return cartRepoPort.saveCart(newCart);
                });

        List<CartItem> items = cartRepoPort.findItemsByCartId(cart.getId());
        cart.setItems(items);
        return cart;
    }

    @Override
    @Transactional
    public Cart addItemToCart(UUID userId, UUID productId, UUID variantId, Integer quantity) {
        if (quantity <= 0) {
            throw new DomainException("INVALID_QUANTITY", "Số lượng sản phẩm thêm vào giỏ phải lớn hơn 0");
        }

        Cart cart = getOrCreateCart(userId);

        // Kiểm tra xem sản phẩm/biến thể này đã nằm trong giỏ chưa
        CartItem item = cartRepoPort.findItemInCart(cart.getId(), productId, variantId)
                .orElse(CartItem.builder()
                        .cartId(cart.getId())
                        .productId(productId)
                        .variantId(variantId)
                        .quantity(0)
                        .build());

        // Cộng dồn số lượng hàng
        item.setQuantity(item.getQuantity() + quantity);
        cartRepoPort.saveItem(item);

        // Trả về thông tin giỏ hàng mới nhất
        List<CartItem> updatedItems = cartRepoPort.findItemsByCartId(cart.getId());
        cart.setItems(updatedItems);
        return cart;
    }

    @Override
    @Transactional
    public Cart updateItemQuantity(UUID userId, UUID productId, UUID variantId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartRepoPort.findItemInCart(cart.getId(), productId, variantId)
                .orElseThrow(() -> new DomainException("ITEM_NOT_FOUND_IN_CART", "Sản phẩm không tồn tại trong giỏ hàng"));

        if (quantity <= 0) {
            // Nếu số lượng cập nhật nhỏ hơn hoặc bằng 0, tiến hành xóa sản phẩm khỏi giỏ hàng
            cartRepoPort.deleteItemById(item.getId());
        } else {
            item.setQuantity(quantity);
            cartRepoPort.saveItem(item);
        }

        List<CartItem> updatedItems = cartRepoPort.findItemsByCartId(cart.getId());
        cart.setItems(updatedItems);
        return cart;
    }

    @Override
    @Transactional
    public Cart removeItemFromCart(UUID userId, UUID productId, UUID variantId) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartRepoPort.findItemInCart(cart.getId(), productId, variantId)
                .orElseThrow(() -> new DomainException("ITEM_NOT_FOUND_IN_CART", "Sản phẩm không có trong giỏ hàng để xóa"));

        cartRepoPort.deleteItemById(item.getId());

        List<CartItem> updatedItems = cartRepoPort.findItemsByCartId(cart.getId());
        cart.setItems(updatedItems);
        return cart;
    }
}