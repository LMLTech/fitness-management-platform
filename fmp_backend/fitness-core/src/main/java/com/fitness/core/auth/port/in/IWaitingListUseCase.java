package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Booking;
import java.util.UUID;

public interface IWaitingListUseCase {
    // Luồng hủy chỗ cũ của một người và tự động đôn người trong hàng chờ lên thế suất
    void cancelBookingAndPromoteNext(UUID memberUserId, UUID bookingId);
}