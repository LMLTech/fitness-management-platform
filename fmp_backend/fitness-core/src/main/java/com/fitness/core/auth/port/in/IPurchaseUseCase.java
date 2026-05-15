package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Subscription;
import java.util.UUID;

public interface IPurchaseUseCase {
    // Luồng mua gói tập: Trả về Subscription vừa tạo
    Subscription purchasePlan(UUID memberId, UUID planId, String paymentMethod);
}