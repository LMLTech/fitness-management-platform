package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanPermissionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID tự tăng
    private Long id;

    @Column(name = "permission_key", nullable = false, length = 100)
    // Tên quyền của gói tập
    // VD: ACCESS_SWIMMING_POOL, MAX_GUEST_PASSES
    private String permissionKey;

    @Column(name = "permission_value", nullable = false, length = 255)
    // Giá trị quyền
    // VD: TRUE, FALSE, 5, UNLIMITED
    private String permissionValue;
}