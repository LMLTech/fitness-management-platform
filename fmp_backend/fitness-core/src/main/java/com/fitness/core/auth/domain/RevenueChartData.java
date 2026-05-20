package com.fitness.core.auth.domain;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueChartData {
    private List<String> labels;
    private List<BigDecimal> data;
}