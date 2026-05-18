package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Order;
import com.fitness.core.auth.domain.OrderItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderRepositoryPort {
    Order saveOrder(Order order);
    Optional<Order> findOrderById(UUID id);
    List<Order> findOrdersByUserId(UUID userId);
    void saveOrderItems(List<OrderItem> items);
    List<OrderItem> findItemsByOrderId(UUID orderId);

    // Hàm phụ trợ để dọn giỏ hàng sau khi checkout
    void clearCartItems(UUID userId);
}