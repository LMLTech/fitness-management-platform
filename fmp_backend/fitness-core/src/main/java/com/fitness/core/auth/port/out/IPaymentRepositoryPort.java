package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Payment;
import java.util.Optional;
import java.util.UUID;

public interface IPaymentRepositoryPort {
    Payment save(Payment payment);
    Optional<Payment> findById(UUID id);
}