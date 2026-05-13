package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "receptionists")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReceptionistJpaEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;
    private String shift;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}