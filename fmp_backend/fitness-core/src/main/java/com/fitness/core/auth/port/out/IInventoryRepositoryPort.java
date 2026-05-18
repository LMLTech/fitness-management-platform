package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.BranchProduct;
import com.fitness.core.auth.domain.InventoryLog;
import java.util.Optional;
import java.util.UUID;

public interface IInventoryRepositoryPort {
    Optional<BranchProduct> findStock(UUID branchId, UUID productId, UUID variantId);
    BranchProduct saveStock(BranchProduct branchProduct);
    InventoryLog saveLog(InventoryLog log);

    // CHECK CHI NHÁNH TỒN TẠI
    boolean existsBranchById(UUID branchId);
}