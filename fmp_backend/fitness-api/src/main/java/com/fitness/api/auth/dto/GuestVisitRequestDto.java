package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class GuestVisitRequestDto {
    private String fullName;
    private String phoneNumber;
    private String visitType; // Trial, Guest_Of_Member
    private UUID accompaniedByMemberId;
}