package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberJpaEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    // Quan hệ 1-1, lấy ID của Users làm ID của mình
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "member_code", unique = true, nullable = false, length = 20)
    private String memberCode;

    @Column(name = "health_notes", columnDefinition = "TEXT")
    private String healthNotes;

    @Column(name = "fitness_goals", columnDefinition = "TEXT")
    private String fitnessGoals;

    @Column(name = "referral_code", unique = true, length = 20)
    private String referralCode;

    @Column(name = "referred_by")
    private UUID referredBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}