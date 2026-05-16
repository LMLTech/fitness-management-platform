package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
public class SubscriptionFreezeLog {
    private UUID id;
    private UUID subscriptionId;
    private LocalDate freezeStart;
    private LocalDate freezeEnd;
    private String reason;
    private UUID approvedBy;
    private String status; // 'Pending', 'Approved', 'Rejected'
}