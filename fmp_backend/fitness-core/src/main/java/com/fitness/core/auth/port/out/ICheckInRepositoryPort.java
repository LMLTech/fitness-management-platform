package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.CheckInLog;
import com.fitness.core.auth.domain.GuestVisit;

public interface ICheckInRepositoryPort {
    CheckInLog saveLog(CheckInLog log);
    GuestVisit saveGuestVisit(GuestVisit guestVisit);
}