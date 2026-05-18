package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Order;
import com.fitness.core.auth.domain.OrderItem;
import com.fitness.core.auth.port.out.IOrderRepositoryPort;
import com.fitness.infrastructure.auth.entity.OrderJpaEntity;
import com.fitness.infrastructure.auth.entity.OrderItemJpaEntity;
import com.fitness.infrastructure.auth.entity.CartJpaEntity;
import com.fitness.infrastructure.auth.repository.OrderJpaRepository;
import com.fitness.infrastructure.auth.repository.OrderItemJpaRepository;
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
public class OrderMysqlAdapter implements IOrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;
    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public Order saveOrder(Order order) {
        OrderJpaEntity entity = OrderJpaEntity.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .branchId(order.getBranchId())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .orderStatus(order.getOrderStatus())
                .shippingAddressId(order.getShippingAddressId())
                .createdAt(order.getCreatedAt())
                .deletedAt(order.getDeletedAt())
                .build();
        OrderJpaEntity saved = orderJpaRepository.saveAndFlush(entity);
        order.setId(saved.getId());
        return order;
    }

    @Override
    public Optional<Order> findOrderById(UUID id) {
        return orderJpaRepository.findById(id).filter(e -> e.getDeletedAt() == null)
                .map(e -> Order.builder()
                        .id(e.getId())
                        .orderNumber(e.getOrderNumber())
                        .userId(e.getUserId())
                        .branchId(e.getBranchId())
                        .totalAmount(e.getTotalAmount())
                        .discountAmount(e.getDiscountAmount())
                        .finalAmount(e.getFinalAmount())
                        .orderStatus(e.getOrderStatus())
                        .shippingAddressId(e.getShippingAddressId())
                        .createdAt(e.getCreatedAt())
                        .build());
    }

    @Override
    public List<Order> findOrdersByUserId(UUID userId) {
        return orderJpaRepository.findAllByUserIdAndDeletedAtIsNull(userId).stream()
                .map(e -> Order.builder()
                        .id(e.getId())
                        .orderNumber(e.getOrderNumber())
                        .userId(e.getUserId())
                        .branchId(e.getBranchId())
                        .totalAmount(e.getTotalAmount())
                        .discountAmount(e.getDiscountAmount())
                        .finalAmount(e.getFinalAmount())
                        .orderStatus(e.getOrderStatus())
                        .createdAt(e.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void saveOrderItems(List<OrderItem> items) {
        List<OrderItemJpaEntity> entities = items.stream()
                .map(v -> OrderItemJpaEntity.builder()
                        .id(v.getId())
                        .orderId(v.getOrderId())
                        .productId(v.getProductId())
                        .variantId(v.getVariantId())
                        .quantity(v.getQuantity())
                        .unitPrice(v.getUnitPrice())
                        .build())
                .collect(Collectors.toList());
        orderItemJpaRepository.saveAll(entities);
    }

    @Override
    public List<OrderItem> findItemsByOrderId(UUID orderId) {
        return orderItemJpaRepository.findAllByOrderIdAndDeletedAtIsNull(orderId).stream()
                .map(e -> OrderItem.builder()
                        .id(e.getId())
                        .orderId(e.getOrderId())
                        .productId(e.getProductId())
                        .variantId(e.getVariantId())
                        .quantity(e.getQuantity())
                        .unitPrice(e.getUnitPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void clearCartItems(UUID userId) {
        Optional<CartJpaEntity> cartOpt = cartJpaRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            var items = cartItemJpaRepository.findAllByCartId(cartOpt.get().getId());
            cartItemJpaRepository.deleteAll(items);
        }
    }
}