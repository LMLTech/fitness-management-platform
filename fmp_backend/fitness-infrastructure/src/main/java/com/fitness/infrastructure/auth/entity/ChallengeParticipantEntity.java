package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "challenge_participants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "challenge_id")
    private UUID challengeId;
    @Column(name = "member_id")
    private UUID memberId;
    @Column(columnDefinition = "JSON")
    private String progress;
    private String status;
}