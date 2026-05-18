package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Cart {
    private UUID id;
    private UUID userId;
    private List<CartItem> items;
}