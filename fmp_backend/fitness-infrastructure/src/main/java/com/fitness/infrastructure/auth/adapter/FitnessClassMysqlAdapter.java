package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.FitnessClass;
import com.fitness.core.auth.port.out.IFitnessClassRepositoryPort;
import com.fitness.infrastructure.auth.entity.FitnessClassJpaEntity;
import com.fitness.infrastructure.auth.repository.FitnessClassJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component // Đánh dấu Adapter để Spring quản lý Bean
@RequiredArgsConstructor // Tự inject constructor cho final field
public class FitnessClassMysqlAdapter implements IFitnessClassRepositoryPort {

    // Gọi xuống JPA Repository để thao tác DB
    private final FitnessClassJpaRepository jpaRepository;

    @Override
    public FitnessClass save(FitnessClass fitnessClass) {
        // Map Domain -> JPA Entity
        FitnessClassJpaEntity entity = FitnessClassJpaEntity.builder()
                .id(fitnessClass.getId())
                .name(fitnessClass.getName())
                .description(fitnessClass.getDescription())
                .classType(fitnessClass.getClassType())
                .difficulty(fitnessClass.getDifficulty())
                .defaultMaxCapacity(fitnessClass.getDefaultMaxCapacity())
                .deletedAt(fitnessClass.getDeletedAt())
                .build();

        // Lưu vào database
        FitnessClassJpaEntity saved = jpaRepository.save(entity);

        // Map ngược Entity -> Domain
        return mapToDomain(saved);
    }

    @Override
    public Optional<FitnessClass> findById(UUID id) {
        // Tìm lớp học theo ID
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<FitnessClass> findByName(String name) {
        // Tìm lớp học theo tên
        return jpaRepository.findByName(name).map(this::mapToDomain);
    }

    @Override
    public List<FitnessClass> findAll() {
        // Lấy tất cả lớp học
        return jpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        // Kiểm tra tên lớp đã tồn tại chưa
        return jpaRepository.existsByName(name);
    }

    @Override
    public void delete(UUID id) {
        // Soft delete: không xóa thật, chỉ gán deletedAt
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(LocalDateTime.now());
            jpaRepository.save(entity);
        });
    }

    // Hàm chuyển JPA Entity -> Domain
    private FitnessClass mapToDomain(FitnessClassJpaEntity entity) {
        return FitnessClass.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .classType(entity.getClassType())
                .difficulty(entity.getDifficulty())
                .defaultMaxCapacity(entity.getDefaultMaxCapacity())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}