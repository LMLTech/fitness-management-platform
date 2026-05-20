package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.TicketMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TicketMessageJpaRepository extends JpaRepository<TicketMessageEntity, UUID> {
    List<TicketMessageEntity> findByTicketId(UUID ticketId);
}