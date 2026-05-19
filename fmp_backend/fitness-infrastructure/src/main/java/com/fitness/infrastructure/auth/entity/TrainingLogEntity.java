package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "training_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingLogEntity {
    @Id
    private UUID id;

    @Column(name = "pt_session_id", nullable = false)
    private UUID ptSessionId;

    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    @Column(nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private Double weight;

    @Column(columnDefinition = "TEXT")
    private String notes;
}