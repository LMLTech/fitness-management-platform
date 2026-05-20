package com.fitness.core.auth.domain;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverview {
    // 1. Tổng quan Doanh thu
    private BigDecimal totalRevenueToday;
    private BigDecimal totalRevenueThisMonth;

    // 2. Tổng quan Hội viên
    private Integer newMembersThisMonth;
    private Integer activeMembersCount;

    // 3. Vận hành
    private Integer totalCheckInsToday;
    private Integer pendingSupportTickets;
}