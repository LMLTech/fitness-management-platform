package com.fitness.api.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DashboardOverviewResponseDto {
    private BigDecimal totalRevenueToday;
    private BigDecimal totalRevenueThisMonth;
    private Integer newMembersThisMonth;
    private Integer activeMembersCount;
    private Integer totalCheckInsToday;
    private Integer pendingSupportTickets;
}