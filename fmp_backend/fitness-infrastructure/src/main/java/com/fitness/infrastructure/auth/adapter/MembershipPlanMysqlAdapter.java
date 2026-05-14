package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.MembershipPlan;
import com.fitness.core.auth.domain.PlanPermission;
import com.fitness.core.auth.port.out.IMembershipPlanRepositoryPort;
import com.fitness.infrastructure.auth.entity.MembershipPlanJpaEntity;
import com.fitness.infrastructure.auth.entity.PlanPermissionJpaEntity;
import com.fitness.infrastructure.auth.repository.MembershipPlanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MembershipPlanMysqlAdapter implements IMembershipPlanRepositoryPort {

    private final MembershipPlanJpaRepository jpaRepository;

    @Override
    public MembershipPlan save(MembershipPlan plan) {
        List<PlanPermissionJpaEntity> permissionEntities = plan.getPermissions().stream()
                .map(p -> PlanPermissionJpaEntity.builder()
                        .id(p.getId())
                        .permissionKey(p.getPermissionKey())
                        .permissionValue(p.getPermissionValue())
                        .build())
                .collect(Collectors.toList());

        MembershipPlanJpaEntity entity = MembershipPlanJpaEntity.builder()
                .id(plan.getId())
                .name(plan.getName())
                .basePrice(plan.getBasePrice())
                .durationMonths(plan.getDurationMonths())
                .planType(plan.getPlanType())
                .maxSessionsPerMonth(plan.getMaxSessionsPerMonth())
                .permissions(permissionEntities)
                .deletedAt(plan.getDeletedAt())
                .build();

        MembershipPlanJpaEntity saved = jpaRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<MembershipPlan> findById(UUID id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<MembershipPlan> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(LocalDateTime.now());
            jpaRepository.save(entity);
        });
    }

    private MembershipPlan mapToDomain(MembershipPlanJpaEntity entity) {
        List<PlanPermission> domainPermissions = entity.getPermissions().stream()
                .map(p -> PlanPermission.builder()
                        .id(p.getId())
                        .permissionKey(p.getPermissionKey())
                        .permissionValue(p.getPermissionValue())
                        .build())
                .collect(Collectors.toList());

        return MembershipPlan.builder()
                .id(entity.getId())
                .name(entity.getName())
                .basePrice(entity.getBasePrice())
                .durationMonths(entity.getDurationMonths())
                .planType(entity.getPlanType())
                .maxSessionsPerMonth(entity.getMaxSessionsPerMonth())
                .permissions(domainPermissions)
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}