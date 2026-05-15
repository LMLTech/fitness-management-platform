package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class PurchaseRequestDto {
    private UUID planId;
    private String paymentMethod; // VD: 'VNPay', 'MoMo'
}