package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Product;
import com.fitness.core.auth.domain.ProductCategory;
import java.util.List;
import java.util.UUID;

public interface IProductUseCase {
    ProductCategory createCategory(String name, UUID parentId);
    List<ProductCategory> getAllCategories();

    Product createProduct(Product product);
    Product getProductById(UUID id);
    List<Product> getAllProducts();
    void deleteProduct(UUID id);
}