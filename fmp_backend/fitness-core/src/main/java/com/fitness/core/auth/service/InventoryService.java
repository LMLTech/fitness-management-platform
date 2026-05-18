package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.BranchProduct;
import com.fitness.core.auth.domain.InventoryLog;
import com.fitness.core.auth.port.in.IInventoryUseCase;
import com.fitness.core.auth.port.out.IInventoryRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryUseCase {

    private final IInventoryRepositoryPort inventoryRepoPort;

    @Override
    @Transactional
    public BranchProduct adjustStock(UUID branchId, UUID productId, UUID variantId, String changeType, Integer quantity, UUID staffId) {
        // CHẶN ĐỨNG MÃ GIẢ: Kiểm tra chi nhánh có tồn tại thật trong DB không trước khi làm việc khác
        if (!inventoryRepoPort.existsBranchById(branchId)) {
            throw new DomainException("BRANCH_NOT_FOUND", "Chi nhánh có mã " + branchId + " không tồn tại trên hệ thống!");
        }

        if (quantity <= 0) {
            throw new DomainException("INVALID_QUANTITY", "Số lượng hàng thay đổi phải lớn hơn 0");
        }

        // 1. Tìm xem dòng sản phẩm/biến thể này đã từng có tại chi nhánh này chưa
        BranchProduct stock = inventoryRepoPort.findStock(branchId, productId, variantId)
                .orElse(BranchProduct.builder()
                        .branchId(branchId)
                        .productId(productId)
                        .variantId(variantId)
                        .stockQuantity(0) // Nếu chưa có thì mặc định bằng 0 để cộng dồn
                        .build());

        // 2. Tính toán số lượng tồn kho mới dựa trên loại nghiệp vụ xuất/nhập
        int currentStock = stock.getStockQuantity();
        if ("IMPORT".equalsIgnoreCase(changeType)) {
            stock.setStockQuantity(currentStock + quantity);
        } else if ("EXPORT".equalsIgnoreCase(changeType)) {
            if (currentStock < quantity) {
                throw new DomainException("OUT_OF_STOCK", "Hàng tồn kho không đủ để xuất! Hiện tại chỉ còn: " + currentStock);
            }
            stock.setStockQuantity(currentStock - quantity);
        } else if ("ADJUST".equalsIgnoreCase(changeType)) {
            // Nghiệp vụ kiểm kê/điều chỉnh trực tiếp số lượng tồn kho thực tế
            stock.setStockQuantity(quantity);
        } else {
            throw new DomainException("UNKNOWN_CHANGE_TYPE", "Loại thay đổi kho không hợp lệ. Phải là IMPORT, EXPORT hoặc ADJUST");
        }

        // 3. Lưu dữ liệu tồn kho mới cập nhật
        BranchProduct savedStock = inventoryRepoPort.saveStock(stock);

        // 4. Ghi nhận lịch sử chi tiết vào bảng nhật ký hệ thống inventory_logs
        InventoryLog log = InventoryLog.builder()
                .branchId(branchId)
                .productId(productId)
                .variantId(variantId)
                .changeType(changeType.toUpperCase())
                .quantityChange(quantity)
                .createdBy(staffId)
                .createdAt(LocalDateTime.now())
                .build();
        inventoryRepoPort.saveLog(log);

        return savedStock;
    }
}