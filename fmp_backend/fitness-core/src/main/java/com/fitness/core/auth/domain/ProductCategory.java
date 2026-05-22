package com.fitness.core.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    private UUID id;
    private String name;
    private String slug;
    private UUID parentId;
    private LocalDateTime deletedAt;
}