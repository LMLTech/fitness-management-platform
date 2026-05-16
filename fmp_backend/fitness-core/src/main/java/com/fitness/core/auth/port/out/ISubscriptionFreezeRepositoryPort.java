package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.SubscriptionFreezeLog;
import java.util.Optional;
import java.util.UUID;

public interface ISubscriptionFreezeRepositoryPort {
    SubscriptionFreezeLog save(SubscriptionFreezeLog log);
    Optional<SubscriptionFreezeLog> findById(UUID id);
}