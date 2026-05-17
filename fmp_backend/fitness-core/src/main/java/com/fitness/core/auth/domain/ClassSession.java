package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ClassSession {
    private UUID id;
    private UUID classId;
    private UUID trainerId;
    private UUID branchId;
    private UUID roomId;
    private LocalDate date;
    private LocalTime startTime;
    private Integer maxCapacity;
    private String status; // 'Scheduled', 'Ongoing', 'Completed', 'Cancelled'
}