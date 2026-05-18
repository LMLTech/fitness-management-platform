package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductCategory {
    private UUID id;
    private String name;
    private String slug;
    private UUID parentId;
    private LocalDateTime deletedAt;
}