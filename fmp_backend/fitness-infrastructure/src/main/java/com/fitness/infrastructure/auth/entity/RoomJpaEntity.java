package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at IS NULL") // Tự động lọc các phòng đã xóa
public class RoomJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "branch_id", nullable = false)
    private UUID branchId;

    @Column(nullable = false, length = 100)
    private String name;

    private Integer capacity;

    @Column(columnDefinition = "TEXT")
    private String facilities;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}