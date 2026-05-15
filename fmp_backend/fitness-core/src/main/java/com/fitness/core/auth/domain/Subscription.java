package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    private UUID id;
    private UUID memberId;
    private UUID planId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // 'Active', 'Expired', 'Frozen', 'Cancelled', 'Pending'
}