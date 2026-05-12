package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Branch;
import com.fitness.core.auth.port.out.IBranchRepositoryPort;
import com.fitness.infrastructure.auth.entity.BranchJpaEntity;
import com.fitness.infrastructure.auth.repository.BranchJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BranchMysqlAdapter implements IBranchRepositoryPort {

    private final BranchJpaRepository repository;

    @Override
    public Branch save(Branch branch) {
        BranchJpaEntity entity = BranchJpaEntity.builder()
                .id(branch.getId())
                .name(branch.getName())
                .code(branch.getCode())
                .phone(branch.getPhone())
                .email(branch.getEmail())
                .isHeadquarters(branch.isHeadquarters())
                .build();
        return mapToDomain(repository.save(entity));
    }

    @Override
    public List<Branch> findAllActive() {
        return repository.findAllActive().stream().map(this::mapToDomain).toList();
    }

    @Override
    public Optional<Branch> findById(UUID id) {
        return repository.findById(id).filter(b -> b.getDeletedAt() == null).map(this::mapToDomain);
    }

    @Override
    public Optional<Branch> findByCode(String code) {
        return repository.findByCode(code).map(this::mapToDomain);
    }

    @Override
    public void softDelete(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(LocalDateTime.now());
            repository.save(entity);
        });
    }

    private Branch mapToDomain(BranchJpaEntity entity) {
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .isHeadquarters(entity.isHeadquarters())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}