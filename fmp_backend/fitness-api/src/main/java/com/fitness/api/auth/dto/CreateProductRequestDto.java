package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateProductRequestDto {
    private UUID categoryId;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal costPrice;
    private List<String> imageUrls;
    private List<VariantDto> variants;

    @Getter
    @Setter
    public static class VariantDto {
        private String sku;
        private String attributes; // Nhận chuỗi JSON (Ví dụ: "{\"size\":\"M\",\"color\":\"Red\"}")
        private BigDecimal priceAdjustment;
    }
}