package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.ChallengeParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeParticipantJpaRepository extends JpaRepository<ChallengeParticipantEntity, UUID> {
    Optional<ChallengeParticipantEntity> findByMemberIdAndChallengeId(UUID memberId, UUID challengeId);
}