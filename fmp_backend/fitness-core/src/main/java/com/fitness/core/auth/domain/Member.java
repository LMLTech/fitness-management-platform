package com.fitness.core.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private UUID userId;
    private UUID branchId;
    private String memberCode;
    private String healthNotes;
    private String fitnessGoals;
    private String referralCode;
    private UUID referredBy;
    private LocalDateTime deletedAt;
}