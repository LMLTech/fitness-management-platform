package com.fitness.core.auth.port.out;

import java.math.BigDecimal;
import java.util.Map;

public interface IReportRepositoryPort {
    BigDecimal calculateRevenueToday();
    BigDecimal calculateRevenueThisMonth();
    Integer countNewMembersThisMonth();
    Integer countActiveMembers();
    Integer countCheckInsToday();
    Integer countPendingSupportTickets();

    Map<String, BigDecimal> getRevenueLast7Days();
}