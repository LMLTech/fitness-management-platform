package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.CreateOrderRequestDto;
import com.fitness.api.auth.dto.CreatePosOrderRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Order;
import com.fitness.core.auth.domain.PosItem;
import com.fitness.core.auth.port.in.IOrderUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderUseCase orderUseCase;
    private final IUserRepositoryPort userRepoPort;

    private UUID getAuthenticatedUserId() {
        String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepoPort.findByEmail(currentEmail)
                .map(u -> u.getId())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Tai khoan khong hop le"));
    }

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Order>> checkoutOnline(@RequestBody CreateOrderRequestDto dto) {
        UUID userId = getAuthenticatedUserId();
        //  Thêm tham số dto.getPaymentMethod() vào cuối cùng
        Order order = orderUseCase.checkoutFromCart(
                userId,
                dto.getBranchId(),
                dto.getShippingAddressId(),
                dto.getPaymentMethod()
        );

        return ResponseEntity.ok(ApiResponse.success(order, "Dat don mua hang truc tuyen thanh cong!"));
    }

    @PostMapping("/pos")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Order>> createPosOrder(@RequestBody CreatePosOrderRequestDto dto) {
        UUID staffId = getAuthenticatedUserId();

        // Bóc tách map dữ liệu từ DTO của tầng API sang Domain thuần của tầng Core
        List<PosItem> coreItems = dto.getItems().stream()
                .map(item -> new PosItem(item.getProductId(), item.getVariantId(), item.getQuantity()))
                .collect(Collectors.toList());

        Order order = orderUseCase.createPosOrder(dto.getBranchId(), dto.getCustomerUserId(), coreItems, staffId);
        return ResponseEntity.ok(ApiResponse.success(order, "Xuat hoa don ban hang tai quay POS thanh cong!"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(ApiResponse.success(orderUseCase.getOrderById(id), "Tai thong tin chi tiet don hang thanh cong"));
    }

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<Order>>> getMyHistory() {
        UUID userId = getAuthenticatedUserId();
        return ResponseEntity.ok(ApiResponse.success(orderUseCase.getMemberOrderHistory(userId), "Tai lich su mua hang thanh cong"));
    }
}