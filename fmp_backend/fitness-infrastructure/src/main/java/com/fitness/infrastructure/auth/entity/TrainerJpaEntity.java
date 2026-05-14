package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerJpaEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId; // Lấy ID của User làm Khóa chính luôn (OneToOne)

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String certifications;

    @Column(name = "commission_rate", precision = 5, scale = 2)
    private BigDecimal commissionRate;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Xử lý quan hệ Nhiều - Nhiều với bảng Specialties
    // Hibernate sẽ tự động tạo ra bảng trung gian "trainer_specialties"
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trainer_specialties",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private Set<SpecialtyJpaEntity> specialties;
}