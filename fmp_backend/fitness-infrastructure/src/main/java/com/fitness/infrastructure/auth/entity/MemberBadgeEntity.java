package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "member_badges")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBadgeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Column(name = "badge_id", nullable = false)
    private UUID badgeId;

    @Column(name = "earned_at")
    private LocalDateTime earnedAt;
}