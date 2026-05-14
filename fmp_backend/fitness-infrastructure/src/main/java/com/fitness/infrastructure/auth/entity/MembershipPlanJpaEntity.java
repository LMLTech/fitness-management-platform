package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "membership_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at IS NULL")
public class MembershipPlanJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Column(name = "plan_type", nullable = false, length = 50)
    private String planType;

    @Column(name = "max_sessions_per_month", nullable = false)
    private Integer maxSessionsPerMonth;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Thiết lập mối quan hệ 1-N dữ liệu quyền lợi, tự động xóa con nếu cha bị xóa hoặc cập nhật
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "plan_id")
    private List<PlanPermissionJpaEntity> permissions;
}