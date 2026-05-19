package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Challenge;
import com.fitness.core.auth.domain.MemberPoint;
import com.fitness.core.auth.port.out.IChallengeRepositoryPort;
import com.fitness.infrastructure.auth.entity.ChallengeEntity;
import com.fitness.infrastructure.auth.entity.ChallengeParticipantEntity;
import com.fitness.infrastructure.auth.entity.MemberBadgeEntity;
import com.fitness.infrastructure.auth.entity.MemberPointEntity;
import com.fitness.infrastructure.auth.repository.ChallengeParticipantJpaRepository;
import com.fitness.infrastructure.auth.repository.ChallengeJpaRepository;
import com.fitness.infrastructure.auth.repository.MemberBadgeJpaRepository;
import com.fitness.infrastructure.auth.repository.MemberPointJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChallengeMysqlAdapter implements IChallengeRepositoryPort {

    private final ChallengeJpaRepository challengeJpaRepo;
    private final ChallengeParticipantJpaRepository participantJpaRepo;
    private final MemberPointJpaRepository pointJpaRepo;
    private final MemberBadgeJpaRepository memberBadgeJpaRepo;

    @Override
    public Optional<Challenge> findChallengeById(UUID challengeId) {
        return challengeJpaRepo.findById(challengeId)
                .map(entity -> Challenge.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .rules(entity.getRules())
                        .rewardPoints(entity.getRewardPoints())
                        .targetBadgeId(entity.getTargetBadgeId())
                        .deletedAt(entity.getDeletedAt())
                        .build());
    }

    @Override
    public boolean isAlreadyJoined(UUID memberId, UUID challengeId) {
        return participantJpaRepo.findByMemberIdAndChallengeId(memberId, challengeId).isPresent();
    }

    @Override
    public void saveParticipant(UUID memberId, UUID challengeId, String status, String progress) {
        ChallengeParticipantEntity entity = ChallengeParticipantEntity.builder()
                .memberId(memberId)
                .challengeId(challengeId)
                .status(status)
                .progress(progress)
                .build();
        participantJpaRepo.save(entity);
    }

    @Override
    public void updateParticipantStatus(UUID memberId, UUID challengeId, String status) {
        ChallengeParticipantEntity entity = participantJpaRepo.findByMemberIdAndChallengeId(memberId, challengeId)
                .orElseThrow(() -> new RuntimeException("Hội viên chưa tham gia thử thách này"));
        entity.setStatus(status);
        participantJpaRepo.save(entity);
    }

    @Override
    public void savePointLog(MemberPoint pointLog) {
        MemberPointEntity entity = MemberPointEntity.builder()
                .id(pointLog.getId())
                .memberId(pointLog.getMemberId())
                .pointsChange(pointLog.getPointsChange())
                .reason(pointLog.getReason())
                .createdAt(pointLog.getCreatedAt())
                .build();
        pointJpaRepo.save(entity);
    }

    @Override
    public void saveMemberBadge(UUID memberId, UUID badgeId) {
        MemberBadgeEntity entity = MemberBadgeEntity.builder()
                .memberId(memberId)
                .badgeId(badgeId)
                .earnedAt(LocalDateTime.now())
                .build();
        memberBadgeJpaRepo.save(entity);
    }
}