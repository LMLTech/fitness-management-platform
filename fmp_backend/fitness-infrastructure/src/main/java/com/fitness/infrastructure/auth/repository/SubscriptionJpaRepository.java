package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.SubscriptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, UUID> {
    List<SubscriptionJpaEntity> findByMemberId(UUID memberId);
}