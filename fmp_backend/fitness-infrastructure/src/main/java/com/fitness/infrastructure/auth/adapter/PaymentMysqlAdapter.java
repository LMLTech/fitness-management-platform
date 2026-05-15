package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Payment;
import com.fitness.core.auth.port.out.IPaymentRepositoryPort;
import com.fitness.infrastructure.auth.entity.PaymentJpaEntity;
import com.fitness.infrastructure.auth.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentMysqlAdapter implements IPaymentRepositoryPort {

    private final PaymentJpaRepository repository;

    @Override
    public Payment save(Payment domain) {
        PaymentJpaEntity entity = PaymentJpaEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .subscriptionId(domain.getSubscriptionId())
                .amount(domain.getAmount())
                .paymentMethod(domain.getPaymentMethod())
                .status(domain.getStatus())
                .transactionCode(domain.getTransactionCode())
                .createdAt(domain.getCreatedAt())
                .build();

        PaymentJpaEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    private Payment mapToDomain(PaymentJpaEntity entity) {
        return Payment.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .subscriptionId(entity.getSubscriptionId())
                .amount(entity.getAmount())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .transactionCode(entity.getTransactionCode())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}