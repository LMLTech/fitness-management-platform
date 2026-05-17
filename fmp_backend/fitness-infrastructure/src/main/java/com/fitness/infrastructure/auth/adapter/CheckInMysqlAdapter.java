package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.CheckInLog;
import com.fitness.core.auth.domain.GuestVisit;
import com.fitness.core.auth.port.out.ICheckInRepositoryPort;
import com.fitness.infrastructure.auth.entity.CheckInLogJpaEntity;
import com.fitness.infrastructure.auth.entity.GuestVisitJpaEntity;
import com.fitness.infrastructure.auth.repository.CheckInLogJpaRepository;
import com.fitness.infrastructure.auth.repository.GuestVisitJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CheckInMysqlAdapter implements ICheckInRepositoryPort {

    private final CheckInLogJpaRepository logRepository;
    private final GuestVisitJpaRepository guestRepository;

    @Override
    public CheckInLog saveLog(CheckInLog log) {
        CheckInLogJpaEntity entity = CheckInLogJpaEntity.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .sessionId(log.getSessionId())
                .accessMethod(log.getAccessMethod())
                .checkInTime(log.getCheckInTime())
                .processedBy(log.getProcessedBy())
                .build();
        CheckInLogJpaEntity saved = logRepository.save(entity);
        log.setId(saved.getId());
        return log;
    }

    @Override
    public GuestVisit saveGuestVisit(GuestVisit guestVisit) {
        GuestVisitJpaEntity entity = GuestVisitJpaEntity.builder()
                .id(guestVisit.getId())
                .fullName(guestVisit.getFullName())
                .phoneNumber(guestVisit.getPhoneNumber())
                .checkInLogId(guestVisit.getCheckInLogId())
                .visitType(guestVisit.getVisitType())
                .accompaniedByMemberId(guestVisit.getAccompaniedByMemberId())
                .build();
        GuestVisitJpaEntity saved = guestRepository.save(entity);
        guestVisit.setId(saved.getId());
        return guestVisit;
    }
}