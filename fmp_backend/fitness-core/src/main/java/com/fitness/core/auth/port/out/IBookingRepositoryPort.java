package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Booking;
import java.util.Optional;
import java.util.UUID;

public interface IBookingRepositoryPort {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID id);
    long countConfirmedBookings(UUID sessionId);
    boolean hasMemberBooked(UUID memberId, UUID sessionId);
}