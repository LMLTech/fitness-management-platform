package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.CartItemRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Cart;
import com.fitness.core.auth.port.in.ICartUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final ICartUseCase cartUseCase;
    private final IUserRepositoryPort userRepoPort;

    private UUID getAuthenticatedUserId() {
        String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepoPort.findByEmail(currentEmail)
                .map(u -> u.getId())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Không tìm thấy tài khoản người dùng"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Cart>> getCart() {
        UUID userId = getAuthenticatedUserId();
        Cart cart = cartUseCase.getOrCreateCart(userId);
        return ResponseEntity.ok(ApiResponse.success(cart, "Tải thông tin giỏ hàng thành công!"));
    }

    @PostMapping("/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Cart>> addItemToCart(@RequestBody CartItemRequestDto dto) {
        UUID userId = getAuthenticatedUserId();
        Cart updatedCart = cartUseCase.addItemToCart(userId, dto.getProductId(), dto.getVariantId(), dto.getQuantity());
        return ResponseEntity.ok(ApiResponse.success(updatedCart, "Đã thêm sản phẩm vào giỏ hàng!"));
    }

    @PutMapping("/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Cart>> updateItemQuantity(@RequestBody CartItemRequestDto dto) {
        UUID userId = getAuthenticatedUserId();
        Cart updatedCart = cartUseCase.updateItemQuantity(userId, dto.getProductId(), dto.getVariantId(), dto.getQuantity());
        return ResponseEntity.ok(ApiResponse.success(updatedCart, "Cập nhật số lượng sản phẩm thành công!"));
    }

    @DeleteMapping("/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Cart>> removeItemFromCart(
            @RequestParam("productId") UUID productId,
            @RequestParam(value = "variantId", required = false) UUID variantId) {
        UUID userId = getAuthenticatedUserId();
        Cart updatedCart = cartUseCase.removeItemFromCart(userId, productId, variantId);
        return ResponseEntity.ok(ApiResponse.success(updatedCart, "Đã xóa sản phẩm khỏi giỏ hàng thành công!"));
    }
}