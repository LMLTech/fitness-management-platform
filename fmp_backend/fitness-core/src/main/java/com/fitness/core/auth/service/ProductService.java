package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Product;
import com.fitness.core.auth.domain.ProductCategory;
import com.fitness.core.auth.domain.ProductVariant;
import com.fitness.core.auth.port.in.IProductUseCase;
import com.fitness.core.auth.port.out.IProductRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductUseCase {

    private final IProductRepositoryPort productRepoPort;

    // ==========================================
    // QUẢN LÝ DANH MỤC (CATEGORY)
    // ==========================================
    @Override
    @Transactional
    @CacheEvict(value = "categories_cache", allEntries = true)
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
    @Cacheable(value = "categories_cache", key = "'all_categories'")
    public List<ProductCategory> getAllCategories() {
        System.out.println("======> ĐANG CHẠY XUỐNG MYSQL LẤY DANH MỤC SẢN PHẨM <======");
        return productRepoPort.findAllCategories();
    }

    // ==========================================
    // QUẢN LÝ SẢN PHẨM (PRODUCT)
    // ==========================================
    @Override
    @Transactional
    @CacheEvict(value = "products_cache", allEntries = true)
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
    @Cacheable(value = "products_cache", key = "'product_' + #id")
    public Product getProductById(UUID id) {
        System.out.println("======> ĐANG CHẠY XUỐNG MYSQL LẤY CHI TIẾT SẢN PHẨM: " + id + " <======");
        return productRepoPort.findProductById(id)
                .orElseThrow(() -> new DomainException("PRODUCT_NOT_FOUND", "Sản phẩm không tồn tại hoặc đã bị xóa"));
    }

    @Override
    @Cacheable(value = "products_cache", key = "'all_products'")
    public List<Product> getAllProducts() {
        System.out.println("======> ĐANG CHẠY XUỐNG MYSQL LẤY DANH SÁCH SẢN PHẨM <======");
        return productRepoPort.findAllProducts();
    }

    @Override
    @Transactional
    @CacheEvict(value = "products_cache", allEntries = true)
    public void deleteProduct(UUID id) {
        Product product = getProductById(id); // Dòng này sẽ gọi lấy cache lên, sau đó xuống dưới đánh dấu xóa
        product.setDeletedAt(LocalDateTime.now());
        if (product.getVariants() != null) {
            product.getVariants().forEach(v -> v.setDeletedAt(LocalDateTime.now()));
        }
        productRepoPort.saveProduct(product);
    }
}