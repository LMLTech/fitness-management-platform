package com.fitness.core.auth.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanPermission {

    // ID của quyền khóa chính
    private Long id;

    // Tên quyền của gói tập
    // VD: ACCESS_SWIMMING_POOL, MAX_GUEST_PASSES
    private String permissionKey;

    // Giá trị của quyền
    // VD: TRUE, FALSE, 5, UNLIMITED
    private String permissionValue;
}