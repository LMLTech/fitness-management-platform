package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.ClassSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface ICreateClassSessionUseCase {
    ClassSession scheduleNewSession(UUID classId, UUID trainerId, UUID branchId, UUID roomId, LocalDate date, LocalTime startTime, Integer maxCapacity);
}