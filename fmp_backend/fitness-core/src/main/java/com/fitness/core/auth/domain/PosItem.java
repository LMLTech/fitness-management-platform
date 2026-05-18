package com.fitness.core.auth.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PosItem {
    private UUID productId;
    private UUID variantId;
    private Integer quantity;
}