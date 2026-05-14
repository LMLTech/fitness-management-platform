package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.MembershipPlan;
import java.util.List;
import java.util.UUID;

public interface IMembershipPlanUseCase {
    MembershipPlan createPlan(MembershipPlan plan);
    MembershipPlan updatePlan(UUID id, MembershipPlan plan);
    MembershipPlan getPlanById(UUID id);
    List<MembershipPlan> getAllPlans();
    void deletePlan(UUID id);
}