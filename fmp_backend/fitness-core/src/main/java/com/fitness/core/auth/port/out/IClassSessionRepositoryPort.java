package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.ClassSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface IClassSessionRepositoryPort {
    ClassSession save(ClassSession session);
    Optional<ClassSession> findById(UUID id);
    boolean hasTrainerConflict(UUID trainerId, LocalDate date, LocalTime startTime);
    boolean hasRoomConflict(UUID roomId, LocalDate date, LocalTime startTime);
}