package com.fitness.core.auth.domain;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private UUID id;
    private UUID userId;          // Để lưu thông tin người thanh toán
    private UUID subscriptionId;  // Dành cho luồng mua gói tập
    private UUID orderId;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private String transactionCode;
    private LocalDateTime createdAt;
}