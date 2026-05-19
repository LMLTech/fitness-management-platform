package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "personal_training_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalTrainingSessionEntity {
    @Id
    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Column(columnDefinition = "TEXT")
    private String objectives;
}