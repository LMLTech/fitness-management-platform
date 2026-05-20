package com.fitness.api.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class RevenueChartResponseDto {
    private List<String> labels;
    private List<BigDecimal> data;
}