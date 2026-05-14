package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class TrainerUpdateDto {
    private String bio;
    private String certifications;
    private BigDecimal commissionRate;
    private List<String> specialties; // VD: ["Gym", "Yoga"]
}