package com.fitness.api.auth.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequestDto {
    private UUID branchId;
    private UUID shippingAddressId;
    private String paymentMethod;
}