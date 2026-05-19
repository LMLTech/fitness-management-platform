package com.fitness.core.auth.port.in;

import java.util.UUID;

public interface IChallengeUseCase {
    void joinChallenge(UUID memberId, UUID challengeId);
    void completeChallenge(UUID memberId, UUID challengeId);
}