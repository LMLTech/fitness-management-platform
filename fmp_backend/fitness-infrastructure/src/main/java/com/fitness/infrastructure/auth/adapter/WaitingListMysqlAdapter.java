package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.WaitingList;
import com.fitness.core.auth.port.out.IWaitingListRepositoryPort;
import com.fitness.infrastructure.auth.entity.WaitingListJpaEntity;
import com.fitness.infrastructure.auth.repository.WaitingListJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WaitingListMysqlAdapter implements IWaitingListRepositoryPort {

    private final WaitingListJpaRepository jpaRepository;

    @Override
    public WaitingList save(WaitingList waitingList) {
        WaitingListJpaEntity entity = WaitingListJpaEntity.builder()
                .id(waitingList.getId())
                .memberId(waitingList.getMemberId())
                .sessionId(waitingList.getSessionId())
                .position(waitingList.getPosition())
                .status(waitingList.getStatus())
                .build();
        WaitingListJpaEntity saved = jpaRepository.save(entity);
        waitingList.setId(saved.getId());
        return waitingList;
    }

    @Override
    public int getMaxPosition(UUID sessionId) {
        return jpaRepository.findMaxPositionBySessionId(sessionId);
    }

    @Override
    public Optional<WaitingList> getFirstInQueue(UUID sessionId) {
        List<WaitingListJpaEntity> list = jpaRepository.findBySessionIdAndStatusOrderByPositionAsc(sessionId, "Waiting");
        if (list.isEmpty()) return Optional.empty();

        WaitingListJpaEntity first = list.get(0);
        return Optional.of(WaitingList.builder()
                .id(first.getId())
                .memberId(first.getMemberId())
                .sessionId(first.getSessionId())
                .position(first.getPosition())
                .status(first.getStatus())
                .build());
    }

    @Override
    public boolean isMemberInWaitlist(UUID memberId, UUID sessionId) {
        return jpaRepository.existsByMemberIdAndSessionIdAndStatus(memberId, sessionId, "Waiting");
    }
}