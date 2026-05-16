package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.SubscriptionFreezeLog;
import com.fitness.core.auth.port.out.ISubscriptionFreezeRepositoryPort;
import com.fitness.infrastructure.auth.entity.SubscriptionFreezeLogJpaEntity;
import com.fitness.infrastructure.auth.repository.SubscriptionFreezeLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubscriptionFreezeMysqlAdapter implements ISubscriptionFreezeRepositoryPort {

    private final SubscriptionFreezeLogJpaRepository jpaRepository;

    @Override
    public SubscriptionFreezeLog save(SubscriptionFreezeLog log) {
        SubscriptionFreezeLogJpaEntity entity = SubscriptionFreezeLogJpaEntity.builder()
                .id(log.getId())
                .subscriptionId(log.getSubscriptionId())
                .freezeStart(log.getFreezeStart())
                .freezeEnd(log.getFreezeEnd())
                .reason(log.getReason())
                .approvedBy(log.getApprovedBy())
                .status(log.getStatus())
                .build();

        SubscriptionFreezeLogJpaEntity saved = jpaRepository.save(entity);
        log.setId(saved.getId());
        return log;
    }

    @Override
    public Optional<SubscriptionFreezeLog> findById(UUID id) {
        return jpaRepository.findById(id).map(entity -> SubscriptionFreezeLog.builder()
                .id(entity.getId())
                .subscriptionId(entity.getSubscriptionId())
                .freezeStart(entity.getFreezeStart())
                .freezeEnd(entity.getFreezeEnd())
                .reason(entity.getReason())
                .approvedBy(entity.getApprovedBy())
                .status(entity.getStatus())
                .build());
    }
}