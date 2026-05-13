package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "staffs")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class StaffJpaEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "employee_id", unique = true, nullable = false, length = 20)
    private String employeeId;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}