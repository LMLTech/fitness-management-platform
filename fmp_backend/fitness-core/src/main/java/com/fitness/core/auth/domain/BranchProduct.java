package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Builder
public class BranchProduct {
    private UUID id;
    private UUID branchId;
    private UUID productId;
    private UUID variantId;
    private Integer stockQuantity;
}