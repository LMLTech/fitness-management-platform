package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {
    private Long id;
    private UUID staffId; // Trỏ tới user_id của Staff/User
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
}