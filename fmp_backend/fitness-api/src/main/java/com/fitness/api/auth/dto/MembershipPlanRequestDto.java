package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MembershipPlanRequestDto {

    // Tên gói tập
    // VD: Premium 12 Tháng
    private String name;

    // Giá gốc của gói tập
    // VD: 1999000.00
    private BigDecimal basePrice;

    // Thời hạn gói (tính theo tháng)
    // VD: 1, 3, 6, 12
    private Integer durationMonths;

    // Loại gói tập
    // VD: ALL_ACCESS, CLASS_ONLY
    private String planType;

    // Số buổi tối đa mỗi tháng
    // VD: 30, 50, -1 (không giới hạn)
    private Integer maxSessionsPerMonth;

    // Danh sách quyền lợi của gói tập
    // VD: hồ bơi, guest pass,...
    private List<PlanPermissionDto> permissions;
}