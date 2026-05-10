package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PermissionJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // "USER_VIEW", "USER_EDIT", "CLASS_DELETE"
    private String resource; // "USER", "CLASS"
    private String action;   // "VIEW", "DELETE"
}