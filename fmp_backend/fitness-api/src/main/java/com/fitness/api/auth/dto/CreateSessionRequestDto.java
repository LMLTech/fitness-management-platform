package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class CreateSessionRequestDto {
    private UUID classId;
    private UUID trainerId;
    private UUID branchId;
    private UUID roomId;
    private LocalDate date;
    private LocalTime startTime;
    private Integer maxCapacity;
}