package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.ClassSession;
import com.fitness.core.auth.port.out.IClassSessionRepositoryPort;
import com.fitness.infrastructure.auth.entity.ClassSessionJpaEntity;
import com.fitness.infrastructure.auth.repository.ClassSessionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClassSessionMysqlAdapter implements IClassSessionRepositoryPort {

    private final ClassSessionJpaRepository jpaRepository;

    @Override
    public ClassSession save(ClassSession session) {
        ClassSessionJpaEntity entity = ClassSessionJpaEntity.builder()
                .id(session.getId())
                .classId(session.getClassId())
                .trainerId(session.getTrainerId())
                .branchId(session.getBranchId())
                .roomId(session.getRoomId())
                .date(session.getDate())
                .startTime(session.getStartTime())
                .maxCapacity(session.getMaxCapacity())
                .status(session.getStatus())
                .build();

        ClassSessionJpaEntity saved = jpaRepository.save(entity);
        session.setId(saved.getId());
        return session;
    }

    @Override
    public Optional<ClassSession> findById(UUID id) {
        return jpaRepository.findById(id).map(entity -> ClassSession.builder()
                .id(entity.getId())
                .classId(entity.getClassId())
                .trainerId(entity.getTrainerId())
                .branchId(entity.getBranchId())
                .roomId(entity.getRoomId())
                .date(entity.getDate())
                .startTime(entity.getStartTime())
                .maxCapacity(entity.getMaxCapacity())
                .status(entity.getStatus())
                .build());
    }

    @Override
    public boolean hasTrainerConflict(UUID trainerId, LocalDate date, LocalTime startTime) {
        // Gọi hàm count và so sánh > 0 để chuyển từ int sang boolean
        return jpaRepository.countTrainerConflicts(trainerId, date, startTime) > 0;
    }

    @Override
    public boolean hasRoomConflict(UUID roomId, LocalDate date, LocalTime startTime) {
        // Gọi hàm count và so sánh > 0 để chuyển từ int sang boolean
        return jpaRepository.countRoomConflicts(roomId, date, startTime) > 0;
    }
}