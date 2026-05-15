package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Subscription;
import com.fitness.core.auth.port.out.ISubscriptionRepositoryPort;
import com.fitness.infrastructure.auth.entity.SubscriptionJpaEntity;
import com.fitness.infrastructure.auth.repository.SubscriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionMysqlAdapter implements ISubscriptionRepositoryPort {

    private final SubscriptionJpaRepository repository;

    @Override
    public Subscription save(Subscription domain) {
        SubscriptionJpaEntity entity = SubscriptionJpaEntity.builder()
                .id(domain.getId())
                .memberId(domain.getMemberId())
                .planId(domain.getPlanId())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .status(domain.getStatus())
                .build();

        SubscriptionJpaEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Subscription> findById(UUID id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Subscription> findByMemberId(UUID memberId) {
        return repository.findByMemberId(memberId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private Subscription mapToDomain(SubscriptionJpaEntity entity) {
        return Subscription.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .planId(entity.getPlanId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .build();
    }
}