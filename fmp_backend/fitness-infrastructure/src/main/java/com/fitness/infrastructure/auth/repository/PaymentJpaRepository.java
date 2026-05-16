package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, UUID> {

    // Sử dụng Native Query kết hợp hàm HEX() của MySQL để bóc tách UUID dạng BINARY(16)
    @Query(value = "SELECT * FROM payments WHERE HEX(id) LIKE CONCAT(UPPER(:prefix), '%') AND status = 'Pending'", nativeQuery = true)
    Optional<PaymentJpaEntity> findPendingPaymentByUuidPrefix(@Param("prefix") String prefix);
}