package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Branch {
    private UUID id;
    private String name;
    private String code;
    private String phone;
    private String email;
    private boolean isHeadquarters;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}