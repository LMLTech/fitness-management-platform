package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Order {
    private UUID id;
    private String orderNumber; // Mã hiển thị hóa đơn độc nhất ORD
    private UUID userId;
    private UUID branchId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String orderStatus; // PENDING, COMPLETED, CANCELLED
    private UUID shippingAddressId; // NULL nếu mua tại quầy POS
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private List<OrderItem> items;
}