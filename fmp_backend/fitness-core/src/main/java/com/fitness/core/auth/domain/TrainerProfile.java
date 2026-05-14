package com.fitness.core.auth.domain;

import lombok.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerProfile {
    private UUID userId;
    private String bio;
    private String certifications;
    private BigDecimal commissionRate; // Tỉ lệ phần trăm hoa hồng

    // Danh sách các chuyên môn của HLV này
    private Set<Specialty> specialties;
}