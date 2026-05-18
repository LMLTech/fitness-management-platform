package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Product;
import com.fitness.core.auth.domain.ProductCategory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductRepositoryPort {
    ProductCategory saveCategory(ProductCategory category);
    List<ProductCategory> findAllCategories();

    Product saveProduct(Product product);
    Optional<Product> findProductById(UUID id);
    List<Product> findAllProducts();

    boolean existsProductBySku(String sku);
    boolean existsVariantBySku(String sku);
}