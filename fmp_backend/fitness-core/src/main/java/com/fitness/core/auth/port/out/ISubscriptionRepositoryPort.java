package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Subscription;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ISubscriptionRepositoryPort {
    Subscription save(Subscription subscription);
    Optional<Subscription> findById(UUID id);
    List<Subscription> findByMemberId(UUID memberId);
}