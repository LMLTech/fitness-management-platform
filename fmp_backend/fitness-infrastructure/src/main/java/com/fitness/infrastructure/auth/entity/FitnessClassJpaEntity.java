package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fitness_classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Hỗ trợ tạo object bằng Builder Pattern
@Where(clause = "deleted_at IS NULL") // Tự động bỏ qua dữ liệu đã xóa mềm
public class FitnessClassJpaEntity {

    @Id // Khóa chính
    @GeneratedValue(strategy = GenerationType.UUID) // Tự sinh UUID
    private UUID id;

    @Column(nullable = false, unique = true, length = 100) // Tên lớp, bắt buộc và không trùng
    private String name;

    @Column(columnDefinition = "TEXT") // Mô tả lớp học
    private String description;

    @Column(name = "class_type", nullable = false, length = 50) // Loại lớp (Yoga, Gym, Zumba...)
    private String classType;

    @Column(nullable = false, length = 30) // Mức độ khó (Easy, Medium, Hard)
    private String difficulty;

    @Column(name = "default_max_capacity", nullable = false) // Sức chứa mặc định của lớp
    private Integer defaultMaxCapacity;

    @Column(name = "deleted_at") // Thời gian xóa mềm
    private LocalDateTime deletedAt;
}