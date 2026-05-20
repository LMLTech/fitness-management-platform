package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.DashboardOverview;
import com.fitness.core.auth.domain.RevenueChartData;

public interface IDashboardUseCase {
    DashboardOverview getGeneralOverview();
    RevenueChartData getRevenueLast7Days();
}