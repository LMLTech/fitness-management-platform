package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Order;
import com.fitness.core.auth.domain.PosItem;
import java.util.List;
import java.util.UUID;

public interface IOrderUseCase {
    Order checkoutFromCart(UUID userId, UUID branchId, UUID shippingAddressId, String paymentMethod);
    Order createPosOrder(UUID branchId, UUID customerUserId, List<PosItem> items, UUID staffId);
    Order getOrderById(UUID orderId);
    List<Order> getMemberOrderHistory(UUID userId);
}