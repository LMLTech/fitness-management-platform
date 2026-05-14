package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanPermissionDto {
    // Tên quyền của gói tập
    // VD: ACCESS_SWIMMING_POOL, MAX_GUEST_PASSES
    private String permissionKey;
    // Giá trị của quyền
    // VD: TRUE, FALSE, 5, UNLIMITED
    private String permissionValue;
}