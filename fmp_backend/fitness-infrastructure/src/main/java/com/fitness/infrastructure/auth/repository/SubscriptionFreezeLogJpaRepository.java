package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.SubscriptionFreezeLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SubscriptionFreezeLogJpaRepository extends JpaRepository<SubscriptionFreezeLogJpaEntity, UUID> {
    // Kế thừa các hàm cơ bản CRUD từ JpaRepository
}