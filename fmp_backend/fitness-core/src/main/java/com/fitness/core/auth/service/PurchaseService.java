package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.MembershipPlan;
import com.fitness.core.auth.domain.Payment;
import com.fitness.core.auth.domain.Subscription;
import com.fitness.core.auth.port.in.IPurchaseUseCase;
import com.fitness.core.auth.port.out.IMembershipPlanRepositoryPort;
import com.fitness.core.auth.port.out.IPaymentRepositoryPort;
import com.fitness.core.auth.port.out.ISubscriptionRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseService implements IPurchaseUseCase {

    private final ISubscriptionRepositoryPort subscriptionRepo;
    private final IPaymentRepositoryPort paymentRepo;
    private final IMembershipPlanRepositoryPort planRepo;

    @Override
    @Transactional
    public Subscription purchasePlan(UUID memberId, UUID planId, String paymentMethod) {
        // 1. Lấy thông tin gói tập
        MembershipPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new DomainException("PLAN_NOT_FOUND", "Gói tập không tồn tại"));

        // 2. Khởi tạo Subscription ở trạng thái PENDING
        LocalDate now = LocalDate.now();
        Subscription subscription = Subscription.builder()
                .memberId(memberId)
                .planId(planId)
                .startDate(now)
                .endDate(now.plusMonths(plan.getDurationMonths())) // Tự động tính ngày hết hạn
                .status("Pending")
                .build();

        Subscription savedSubscription = subscriptionRepo.save(subscription);

        // 3. Tạo bản ghi Payment (Hóa đơn chờ)
        Payment payment = Payment.builder()
                .userId(memberId)
                .subscriptionId(savedSubscription.getId())
                .amount(plan.getBasePrice())
                .paymentMethod(paymentMethod)
                .status("Pending")
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepo.save(payment);

        return savedSubscription;
    }
}