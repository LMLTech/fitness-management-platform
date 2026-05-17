package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.CheckInLog;
import com.fitness.core.auth.domain.GuestVisit;
import java.util.UUID;

public interface ICheckInUseCase {
    CheckInLog memberCheckIn(UUID memberUserId, UUID sessionId, String method, UUID staffId);
    GuestVisit registerGuestVisit(String fullName, String phoneNumber, String visitType, UUID accompaniedMemberId, UUID staffId);
}