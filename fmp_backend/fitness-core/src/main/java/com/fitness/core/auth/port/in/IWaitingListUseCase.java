package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Booking;
import java.util.UUID;

public interface IWaitingListUseCase {
    // Luồng hủy chỗ cũ của một người và tự động đôn người trong hàng chờ lên thế suất
    void cancelBookingAndPromoteNext(UUID memberUserId, UUID bookingId);
    // Lấy vị trí hiện tại trong hàng chờ (trả về 0 nếu không có)
    int getMyWaitlistPosition(UUID memberId, UUID sessionId);

    // Hội viên tự động rút lui khỏi danh sách chờ
    void leaveWaitlist(UUID memberId, UUID sessionId);
}