package com.fitness.api.auth.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor // Thêm cho chắc chắn
@AllArgsConstructor
public class TrainerFullResponseDto {
    private UUID userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String employeeId;
    private String jobTitle;
    private String bio;
    private String certifications;
    private BigDecimal commissionRate;
    private Set<String> specialties;
}