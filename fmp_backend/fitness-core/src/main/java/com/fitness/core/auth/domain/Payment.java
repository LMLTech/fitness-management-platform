package com.fitness.core.auth.domain;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private UUID id;
    private UUID userId;
    private UUID subscriptionId;
    private BigDecimal amount;
    private String paymentMethod; // 'CreditCard', 'VNPay', 'MoMo', 'Cash'
    private String status;        // 'Pending', 'Success', 'Failed'
    private String transactionCode;
    private LocalDateTime createdAt;
}