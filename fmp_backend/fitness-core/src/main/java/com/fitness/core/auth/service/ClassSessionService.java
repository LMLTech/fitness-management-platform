package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.ClassSession;
import com.fitness.core.auth.port.in.ICreateClassSessionUseCase;
import com.fitness.core.auth.port.out.IClassSessionRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassSessionService implements ICreateClassSessionUseCase {

    private final IClassSessionRepositoryPort sessionRepoPort;

    @Override
    @Transactional
    public ClassSession scheduleNewSession(UUID classId, UUID trainerId, UUID branchId, UUID roomId,
                                           LocalDate date, LocalTime startTime, Integer maxCapacity) {

        if (date.isBefore(LocalDate.now())) {
            throw new DomainException("INVALID_DATE", "Không được phép xếp lịch dạy vào một ngày trong quá khứ");
        }

        if (date.isEqual(LocalDate.now()) && startTime.isBefore(LocalTime.now())) {
            throw new DomainException("INVALID_TIME", "Giờ bắt đầu buổi học không được nhỏ hơn giờ hiện tại của hệ thống");
        }

        if (maxCapacity <= 0) {
            throw new DomainException("INVALID_CAPACITY", "Sức chứa tối đa của lớp học phải lớn hơn 0");
        }

        if (sessionRepoPort.hasTrainerConflict(trainerId, date, startTime)) {
            throw new DomainException("TRAINER_ALREADY_BOOKED", "Huấn luyện viên này đã có lịch dạy một lớp khác trong khung giờ này");
        }

        if (sessionRepoPort.hasRoomConflict(roomId, date, startTime)) {
            throw new DomainException("ROOM_ALREADY_OCCUPIED", "Phòng tập này đã được sử dụng cho một lớp học khác diễn ra song song");
        }

        ClassSession newSession = ClassSession.builder()
                .classId(classId)
                .trainerId(trainerId)
                .branchId(branchId)
                .roomId(roomId)
                .date(date)
                .startTime(startTime)
                .maxCapacity(maxCapacity)
                .status("Scheduled")
                .build();

        return sessionRepoPort.save(newSession);
    }
}