package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.BranchProduct;
import com.fitness.core.auth.domain.InventoryLog;
import com.fitness.core.auth.port.out.IInventoryRepositoryPort;
import com.fitness.infrastructure.auth.entity.BranchProductJpaEntity;
import com.fitness.infrastructure.auth.entity.InventoryLogJpaEntity;
import com.fitness.infrastructure.auth.repository.BranchProductJpaRepository;
import com.fitness.infrastructure.auth.repository.InventoryLogJpaRepository;
import com.fitness.infrastructure.auth.repository.BranchJpaRepository; // Import repo chi nhánh từ Flow 6
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryMysqlAdapter implements IInventoryRepositoryPort {

    private final BranchProductJpaRepository branchProductJpaRepository;
    private final InventoryLogJpaRepository inventoryLogJpaRepository;
    private final BranchJpaRepository branchJpaRepository;

    @Override
    public Optional<BranchProduct> findStock(UUID branchId, UUID productId, UUID variantId) {
        return branchProductJpaRepository.findStock(branchId, productId, variantId)
                .map(entity -> BranchProduct.builder()
                        .id(entity.getId())
                        .branchId(entity.getBranchId())
                        .productId(entity.getProductId())
                        .variantId(entity.getVariantId())
                        .stockQuantity(entity.getStockQuantity())
                        .build());
    }

    @Override
    public BranchProduct saveStock(BranchProduct branchProduct) {
        BranchProductJpaEntity entity = BranchProductJpaEntity.builder()
                .id(branchProduct.getId())
                .branchId(branchProduct.getBranchId())
                .productId(branchProduct.getProductId())
                .variantId(branchProduct.getVariantId())
                .stockQuantity(branchProduct.getStockQuantity())
                .build();
        BranchProductJpaEntity saved = branchProductJpaRepository.save(entity);
        branchProduct.setId(saved.getId());
        return branchProduct;
    }

    @Override
    public InventoryLog saveLog(InventoryLog log) {
        InventoryLogJpaEntity entity = InventoryLogJpaEntity.builder()
                .id(log.getId())
                .branchId(log.getBranchId())
                .productId(log.getProductId())
                .variantId(log.getVariantId())
                .changeType(log.getChangeType())
                .quantityChange(log.getQuantityChange())
                .createdBy(log.getCreatedBy())
                .createdAt(log.getCreatedAt())
                .build();
        InventoryLogJpaEntity saved = inventoryLogJpaRepository.save(entity);
        log.setId(saved.getId());
        return log;
    }

    // Thực thi hàm check chi nhánh có tồn tại hay không
    @Override
    public boolean existsBranchById(UUID branchId) {
        return branchJpaRepository.existsById(branchId);
    }
}