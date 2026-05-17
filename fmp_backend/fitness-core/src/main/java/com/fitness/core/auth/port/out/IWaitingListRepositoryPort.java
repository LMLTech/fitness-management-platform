package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.WaitingList;
import java.util.Optional;
import java.util.UUID;

public interface IWaitingListRepositoryPort {
    WaitingList save(WaitingList waitingList);
    int getMaxPosition(UUID sessionId);
    Optional<WaitingList> getFirstInQueue(UUID sessionId);
    boolean isMemberInWaitlist(UUID memberId, UUID sessionId);
}