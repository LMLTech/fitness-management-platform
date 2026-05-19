package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Challenge;
import com.fitness.core.auth.domain.MemberPoint;
import java.util.Optional;
import java.util.UUID;

public interface IChallengeRepositoryPort {
    Optional<Challenge> findChallengeById(UUID challengeId);
    boolean isAlreadyJoined(UUID memberId, UUID challengeId);
    void saveParticipant(UUID memberId, UUID challengeId, String status, String progress);
    void updateParticipantStatus(UUID memberId, UUID challengeId, String status);
    void savePointLog(MemberPoint pointLog);
    void saveMemberBadge(UUID memberId, UUID badgeId);
}