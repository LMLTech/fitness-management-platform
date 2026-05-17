package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Builder
public class GuestVisit {
    private UUID id;
    private String fullName;
    private String phoneNumber;
    private UUID checkInLogId;
    private String visitType; // 'Trial', 'Guest_Of_Member'
    private UUID accompaniedByMemberId; // ID hội viên dắt theo (nếu có)
}