package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Challenge;
import com.fitness.core.auth.domain.MemberPoint;
import com.fitness.core.auth.port.in.IChallengeUseCase;
import com.fitness.core.auth.port.out.IChallengeRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChallengeService implements IChallengeUseCase {

    private final IChallengeRepositoryPort challengeRepoPort;

    @Override
    @Transactional
    public void joinChallenge(UUID memberId, UUID challengeId) {
        challengeRepoPort.findChallengeById(challengeId)
                .orElseThrow(() -> new DomainException("CHALLENGE_NOT_FOUND", "Thử thách không tồn tại trên hệ thống"));

        if (challengeRepoPort.isAlreadyJoined(memberId, challengeId)) {
            throw new DomainException("ALREADY_JOINED", "Bạn đã tham gia thử thách này rồi");
        }

        // Khởi tạo progress rỗng hoặc mặc định khi mới tham gia
        challengeRepoPort.saveParticipant(memberId, challengeId, "IN_PROGRESS", "{}");
    }

    @Override
    @Transactional
    public void completeChallenge(UUID memberId, UUID challengeId) {
        Challenge challenge = challengeRepoPort.findChallengeById(challengeId)
                .orElseThrow(() -> new DomainException("CHALLENGE_NOT_FOUND", "Thử thách không tồn tại"));

        // 1. Chuyển trạng thái hoàn thành
        challengeRepoPort.updateParticipantStatus(memberId, challengeId, "COMPLETED");

        // 2. Trả thưởng Điểm
        if (challenge.getRewardPoints() != null && challenge.getRewardPoints() > 0) {
            MemberPoint pointLog = MemberPoint.builder()
                    .id(UUID.randomUUID())
                    .memberId(memberId)
                    .pointsChange(challenge.getRewardPoints())
                    .reason("Hoàn thành thử thách: " + challenge.getName())
                    .createdAt(LocalDateTime.now())
                    .build();
            challengeRepoPort.savePointLog(pointLog);
        }

        // 3. Trả thưởng Huy hiệu Nếu thử thách có cấu hình
        if (challenge.getTargetBadgeId() != null) {
            challengeRepoPort.saveMemberBadge(memberId, challenge.getTargetBadgeId());
        }
    }
}