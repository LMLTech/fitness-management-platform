package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.BranchProduct;
import java.util.UUID;

public interface IInventoryUseCase {
    BranchProduct adjustStock(UUID branchId, UUID productId, UUID variantId, String changeType, Integer quantity, UUID staffId);
}