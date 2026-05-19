package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.EmailCampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EmailCampaignJpaRepository extends JpaRepository<EmailCampaignEntity, UUID> {
}