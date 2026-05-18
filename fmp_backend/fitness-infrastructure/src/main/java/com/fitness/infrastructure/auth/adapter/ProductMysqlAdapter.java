package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Product;
import com.fitness.core.auth.domain.ProductCategory;
import com.fitness.core.auth.domain.ProductVariant;
import com.fitness.core.auth.port.out.IProductRepositoryPort;
import com.fitness.infrastructure.auth.entity.ProductCategoryJpaEntity;
import com.fitness.infrastructure.auth.entity.ProductJpaEntity;
import com.fitness.infrastructure.auth.entity.ProductVariantJpaEntity;
import com.fitness.infrastructure.auth.repository.ProductCategoryJpaRepository;
import com.fitness.infrastructure.auth.repository.ProductJpaRepository;
import com.fitness.infrastructure.auth.repository.ProductVariantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMysqlAdapter implements IProductRepositoryPort {

    private final ProductCategoryJpaRepository categoryRepository;
    private final ProductJpaRepository productRepository;
    private final ProductVariantJpaRepository variantRepository;

    @Override
    public ProductCategory saveCategory(ProductCategory category) {
        ProductCategoryJpaEntity entity = ProductCategoryJpaEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .parentId(category.getParentId())
                .deletedAt(category.getDeletedAt())
                .build();
        ProductCategoryJpaEntity saved = categoryRepository.save(entity);
        category.setId(saved.getId());
        return category;
    }

    @Override
    public List<ProductCategory> findAllCategories() {
        return categoryRepository.findAllByDeletedAtIsNull().stream()
                .map(e -> ProductCategory.builder().id(e.getId()).name(e.getName()).slug(e.getSlug()).parentId(e.getParentId()).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        // 1. Tạo entity cha và lưu xuống trước để lấy ID chuẩn từ DB
        ProductJpaEntity entity = ProductJpaEntity.builder()
                .id(product.getId())
                .categoryId(product.getCategoryId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .costPrice(product.getCostPrice())
                .imageUrls(product.getImageUrls())
                .deletedAt(product.getDeletedAt())
                .build();

        ProductJpaEntity savedProduct = productRepository.saveAndFlush(entity);

        List<ProductVariant> domainVariants = java.util.Collections.emptyList();

        // 2. Kiểm tra và lưu danh sách biến thể con hoàn toàn độc lập
        if (product.getVariants() != null && !product.getVariants().isEmpty()) {
            List<ProductVariantJpaEntity> variantEntities = product.getVariants().stream()
                    .map(v -> ProductVariantJpaEntity.builder()
                            .id(v.getId() != null ? v.getId() : UUID.randomUUID())
                            .productId(savedProduct.getId()) // Khóa ngoại khớp chuẩn với ID cha vừa sinh
                            .sku(v.getSku())
                            .attributes(v.getAttributes())
                            .priceAdjustment(v.getPriceAdjustment())
                            .deletedAt(v.getDeletedAt())
                            .build())
                    .collect(Collectors.toList());

            // Lưu trực tiếp danh sách biến thể xuống DB
            List<ProductVariantJpaEntity> savedVariantEntities = variantRepository.saveAll(variantEntities);

            // Map danh sách vừa lưu thành Domain Model để đóng gói trả về API
            domainVariants = savedVariantEntities.stream()
                    .map(v -> ProductVariant.builder()
                            .id(v.getId())
                            .productId(v.getProductId())
                            .sku(v.getSku())
                            .attributes(v.getAttributes())
                            .priceAdjustment(v.getPriceAdjustment())
                            .build())
                    .collect(Collectors.toList());
        }

        // 3. Đóng gói trả ra đối tượng Domain hoàn chỉnh, bỏ qua trung gian proxy của JPA
        return Product.builder()
                .id(savedProduct.getId())
                .categoryId(savedProduct.getCategoryId())
                .sku(savedProduct.getSku())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .costPrice(savedProduct.getCostPrice())
                .imageUrls(savedProduct.getImageUrls())
                .variants(domainVariants)
                .build();
    }

    @Override
    public Optional<Product> findProductById(UUID id) {
        return productRepository.findById(id).filter(e -> e.getDeletedAt() == null).map(this::mapToDomain);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAllByDeletedAtIsNull().stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsProductBySku(String sku) { return productRepository.existsBySkuAndDeletedAtIsNull(sku); }

    @Override
    public boolean existsVariantBySku(String sku) { return variantRepository.existsBySkuAndDeletedAtIsNull(sku); }

    private Product mapToDomain(ProductJpaEntity entity) {
        List<ProductVariant> variants = entity.getVariants() != null ? entity.getVariants().stream()
                                                                       .filter(v -> v.getDeletedAt() == null)
                                                                       .map(v -> ProductVariant.builder()
                                                                                 .id(v.getId())
                                                                                 .productId(v.getProductId())
                                                                                 .sku(v.getSku())
                                                                                 .attributes(v.getAttributes())
                                                                                 .priceAdjustment(v.getPriceAdjustment())
                                                                                 .build())
                                                                       .collect(Collectors.toList()) : Collections.emptyList();

        return Product.builder()
                .id(entity.getId())
                .categoryId(entity.getCategoryId())
                .sku(entity.getSku())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .costPrice(entity.getCostPrice())
                .imageUrls(entity.getImageUrls())
                .variants(variants)
                .build();
    }
}