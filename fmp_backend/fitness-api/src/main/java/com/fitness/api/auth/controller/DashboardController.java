package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.DashboardOverviewResponseDto;
import com.fitness.api.auth.dto.RevenueChartResponseDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.DashboardOverview;
import com.fitness.core.auth.domain.RevenueChartData;
import com.fitness.core.auth.port.in.IDashboardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardUseCase dashboardUseCase;

    @GetMapping("/overview")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<DashboardOverviewResponseDto>> getOverview() {
        // 1. Gọi UseCase lấy Domain Model
        DashboardOverview domain = dashboardUseCase.getGeneralOverview();

        // 2. Chuyển đổi Domain sang DTO để trả về Client
        DashboardOverviewResponseDto responseDto = DashboardOverviewResponseDto.builder()
                .totalRevenueToday(domain.getTotalRevenueToday())
                .totalRevenueThisMonth(domain.getTotalRevenueThisMonth())
                .newMembersThisMonth(domain.getNewMembersThisMonth())
                .activeMembersCount(domain.getActiveMembersCount())
                .totalCheckInsToday(domain.getTotalCheckInsToday())
                .pendingSupportTickets(domain.getPendingSupportTickets())
                .build();

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Lấy dữ liệu tổng quan thành công"));
    }

    @GetMapping("/charts/revenue-7-days")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<RevenueChartResponseDto>> getRevenueChart() {
        // 1. Gọi UseCase lấy Domain Model
        RevenueChartData domain = dashboardUseCase.getRevenueLast7Days();

        // 2. Chuyển đổi Domain sang DTO
        RevenueChartResponseDto responseDto = RevenueChartResponseDto.builder()
                .labels(domain.getLabels())
                .data(domain.getData())
                .build();

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Lấy dữ liệu biểu đồ doanh thu thành công"));
    }
}