package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.DashboardOverview;
import com.fitness.core.auth.domain.RevenueChartData;
import com.fitness.core.auth.port.in.IDashboardUseCase;
import com.fitness.core.auth.port.out.IReportRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardUseCase {

    private final IReportRepositoryPort reportRepoPort;

    @Override
    public DashboardOverview getGeneralOverview() {
        return DashboardOverview.builder()
                .totalRevenueToday(reportRepoPort.calculateRevenueToday())
                .totalRevenueThisMonth(reportRepoPort.calculateRevenueThisMonth())
                .newMembersThisMonth(reportRepoPort.countNewMembersThisMonth())
                .activeMembersCount(reportRepoPort.countActiveMembers())
                .totalCheckInsToday(reportRepoPort.countCheckInsToday())
                .pendingSupportTickets(reportRepoPort.countPendingSupportTickets())
                .build();
    }

    @Override
    public RevenueChartData getRevenueLast7Days() {
        Map<String, BigDecimal> dailyRevenue = reportRepoPort.getRevenueLast7Days();

        List<String> labels = new ArrayList<>(dailyRevenue.keySet());
        List<BigDecimal> data = new ArrayList<>(dailyRevenue.values());

        return RevenueChartData.builder()
                .labels(labels)
                .data(data)
                .build();
    }
}