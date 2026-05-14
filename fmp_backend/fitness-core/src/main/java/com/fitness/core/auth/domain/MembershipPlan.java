package com.fitness.core.auth.domain;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipPlan {
    private UUID id;
    private String name;               // Tên gói tập (VD: Gói VIP 6 Tháng)
    private BigDecimal basePrice;      // Giá gốc gói tập
    private Integer durationMonths;    // Thời hạn gói (tháng)
    private String planType;           // Loại gói (VD: PERSONAL_TRAINING, CLASS_ONLY, ALL_ACCESS)
    private Integer maxSessionsPerMonth; // Số buổi tập tối đa/tháng (0 nếu không giới hạn)
    private List<PlanPermission> permissions; // Bộ quyền lợi chi tiết của gói
    private LocalDateTime deletedAt;
}