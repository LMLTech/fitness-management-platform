package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "member_points")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointEntity {
    @Id
    private UUID id;
    @Column(name = "member_id")
    private UUID memberId;
    @Column(name = "points_change")
    private Integer pointsChange;
    private String reason;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}