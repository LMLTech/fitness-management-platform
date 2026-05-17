package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Booking;
import com.fitness.core.auth.port.out.IBookingRepositoryPort;
import com.fitness.infrastructure.auth.entity.BookingJpaEntity;
import com.fitness.infrastructure.auth.repository.BookingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingMysqlAdapter implements IBookingRepositoryPort {

    private final BookingJpaRepository jpaRepository;

    @Override
    public Booking save(Booking booking) {
        BookingJpaEntity entity = BookingJpaEntity.builder()
                .id(booking.getId())
                .memberId(booking.getMemberId())
                .sessionId(booking.getSessionId())
                .status(booking.getStatus())
                .checkedInAt(booking.getCheckedInAt())
                .build();

        BookingJpaEntity saved = jpaRepository.save(entity);
        booking.setId(saved.getId());
        return booking;
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return jpaRepository.findById(id).map(entity -> Booking.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .sessionId(entity.getSessionId())
                .status(entity.getStatus())
                .checkedInAt(entity.getCheckedInAt())
                .build());
    }

    @Override
    public long countConfirmedBookings(UUID sessionId) {
        return jpaRepository.countBySessionIdAndStatusAndDeletedAtIsNull(sessionId, "Confirmed");
    }

    @Override
    public boolean hasMemberBooked(UUID memberId, UUID sessionId) {
        return jpaRepository.existsByMemberIdAndSessionIdAndStatusAndDeletedAtIsNull(memberId, sessionId, "Confirmed");
    }
}