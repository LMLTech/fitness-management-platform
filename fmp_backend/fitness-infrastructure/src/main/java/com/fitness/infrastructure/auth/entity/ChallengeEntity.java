package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "challenges")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeEntity {
    @Id
    private UUID id;
    private String name;
    @Column(columnDefinition = "JSON")
    private String rules;
    @Column(name = "reward_points")
    private Integer rewardPoints;
    @Column(name = "target_badge_id")
    private UUID targetBadgeId;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}