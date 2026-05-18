package com.fitness.api.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.api.auth.dto.CreateProductRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Product;
import com.fitness.core.auth.domain.ProductCategory;
import com.fitness.core.auth.domain.ProductVariant;
import com.fitness.core.auth.port.in.IProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductUseCase productUseCase;
    private final ObjectMapper objectMapper;

    @PostMapping("/categories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProductCategory>> createCategory(
            @RequestParam("name") String name,
            @RequestParam(value = "parentId", required = false) UUID parentId) {
        ProductCategory cat = productUseCase.createCategory(name, parentId);
        return ResponseEntity.ok(ApiResponse.success(cat, "Tạo danh mục sản phẩm thành công!"));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(productUseCase.getAllCategories(), "Lấy danh mục thành công"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody CreateProductRequestDto dto) {
        String jsonImages = "[]";
        try {
            if (dto.getImageUrls() != null) {
                jsonImages = objectMapper.writeValueAsString(dto.getImageUrls());
            }
        } catch (Exception ignored) {}

        List<ProductVariant> domainVariants = dto.getVariants() != null ? dto.getVariants().stream()
                                                                          .map(v -> ProductVariant.builder()
                                                                                    .sku(v.getSku())
                                                                                    .attributes(v.getAttributes())
                                                                                    .priceAdjustment(v.getPriceAdjustment())
                                                                                    .build())
                                                                          .collect(Collectors.toList()) : Collections.emptyList();

        Product product = Product.builder()
                .categoryId(dto.getCategoryId())
                .sku(dto.getSku())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .costPrice(dto.getCostPrice())
                .imageUrls(jsonImages)
                .variants(domainVariants)
                .build();

        Product saved = productUseCase.createProduct(product);
        return ResponseEntity.ok(ApiResponse.success(saved, "Thêm mới sản phẩm và các biến thể thành công!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(ApiResponse.success(productUseCase.getProductById(id), "Lấy chi tiết sản phẩm thành công"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success(productUseCase.getAllProducts(), "Lấy danh sách sản phẩm thành công"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable("id") UUID id) {
        productUseCase.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa mềm sản phẩm và toàn bộ biến thể thành công!"));
    }
}