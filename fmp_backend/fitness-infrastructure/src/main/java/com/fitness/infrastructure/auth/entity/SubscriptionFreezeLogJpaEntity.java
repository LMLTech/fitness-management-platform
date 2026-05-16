package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "subscription_freeze_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionFreezeLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "subscription_id", nullable = false)
    private UUID subscriptionId;

    @Column(name = "freeze_start", nullable = false)
    private LocalDate freezeStart;

    @Column(name = "freeze_end", nullable = false)
    private LocalDate freezeEnd;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(nullable = false, length = 20)
    private String status; // Pending, Approved, Rejected
}