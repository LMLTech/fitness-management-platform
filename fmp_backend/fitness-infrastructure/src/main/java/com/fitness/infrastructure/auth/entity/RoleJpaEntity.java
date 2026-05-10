package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RoleJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Phải bắt đầu bằng ROLE_, "ROLE_ADMIN", "ROLE_MEMBER"

    private String description;

    @ManyToMany(fetch = FetchType.EAGER) // Load luôn quyền khi lấy Role
    @JoinTable(
            name = "role_permissions", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionJpaEntity> permissions;
}