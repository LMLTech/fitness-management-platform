package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Product;
import com.fitness.core.auth.domain.ProductCategory;
import com.fitness.core.auth.domain.ProductVariant;
import com.fitness.core.auth.port.in.IProductUseCase;
import com.fitness.core.auth.port.out.IProductRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductUseCase {

    private final IProductRepositoryPort productRepoPort;

    @Override
    @Transactional
    public ProductCategory createCategory(String name, UUID parentId) {
        String slug = name.toLowerCase().trim().replaceAll("[^a-z0-9]", "-").replaceAll("-+", "-");
        ProductCategory category = ProductCategory.builder()
                .name(name)
                .slug(slug)
                .parentId(parentId)
                .build();
        return productRepoPort.saveCategory(category);
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        return productRepoPort.findAllCategories();
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        if (productRepoPort.existsProductBySku(product.getSku())) {
            throw new DomainException("SKU_ALREADY_EXISTS", "Mã SKU sản phẩm gốc đã tồn tại trên hệ thống");
        }

        if (product.getVariants() != null) {
            for (ProductVariant variant : product.getVariants()) {
                if (productRepoPort.existsVariantBySku(variant.getSku())) {
                    throw new DomainException("VARIANT_SKU_EXISTS", "Mã SKU biến thể " + variant.getSku() + " đã tồn tại");
                }
            }
        }
        return productRepoPort.saveProduct(product);
    }

    @Override
    public Product getProductById(UUID id) {
        return productRepoPort.findProductById(id)
                .orElseThrow(() -> new DomainException("PRODUCT_NOT_FOUND", "Sản phẩm không tồn tại hoặc đã bị xóa"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepoPort.findAllProducts();
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        Product product = getProductById(id);
        product.setDeletedAt(LocalDateTime.now());
        if (product.getVariants() != null) {
            product.getVariants().forEach(v -> v.setDeletedAt(LocalDateTime.now()));
        }
        productRepoPort.saveProduct(product);
    }
}