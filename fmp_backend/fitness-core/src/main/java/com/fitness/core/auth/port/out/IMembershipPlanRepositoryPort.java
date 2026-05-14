package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.MembershipPlan;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMembershipPlanRepositoryPort {
    MembershipPlan save(MembershipPlan plan);
    Optional<MembershipPlan> findById(UUID id);
    List<MembershipPlan> findAll();
    boolean existsByName(String name);
    void delete(UUID id);
}