package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Booking;
import java.util.UUID;

public interface IBookingUseCase {
    Booking bookClassSession(UUID memberUserId, UUID sessionId);
}