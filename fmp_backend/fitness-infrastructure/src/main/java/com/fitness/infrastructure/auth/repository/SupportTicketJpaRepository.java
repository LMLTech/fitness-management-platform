package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.SupportTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SupportTicketJpaRepository extends JpaRepository<SupportTicketEntity, UUID> {}