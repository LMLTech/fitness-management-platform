package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Booking;
import com.fitness.core.auth.domain.WaitingList;
import com.fitness.core.auth.port.in.IWaitingListUseCase;
import com.fitness.core.auth.port.out.IBookingRepositoryPort;
import com.fitness.core.auth.port.out.IWaitingListRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WaitingListService implements IWaitingListUseCase {

    private final IBookingRepositoryPort bookingRepoPort;
    private final IWaitingListRepositoryPort waitingListRepoPort;

    @Override
    @Transactional
    public void cancelBookingAndPromoteNext(UUID memberUserId, UUID bookingId) {
        // 1. Lấy thông tin Booking cần hủy lên
        Booking booking = bookingRepoPort.findById(bookingId)
                .orElseThrow(() -> new DomainException("BOOKING_NOT_FOUND", "Không tìm thấy thông tin lịch đặt chỗ này"));

        // Bảo mật: Đảm bảo chính chủ mới được phép hủy lịch của họ
        if (!booking.getMemberId().equals(memberUserId)) {
            throw new DomainException("UNAUTHORIZED_CANCEL", "Bạn không có quyền hủy lịch đặt chỗ của người khác");
        }

        if ("Cancelled".equalsIgnoreCase(booking.getStatus())) {
            throw new DomainException("ALREADY_CANCELLED", "Lịch đặt chỗ này đã được hủy từ trước");
        }

        // 2. Chuyển trạng thái booking sang Cancelled để nhả slot trống ra
        booking.setStatus("Cancelled");
        bookingRepoPort.save(booking);

        // 3. THUẬT TOÁN ĐÔN HÀNG CHỜ: Tìm người đứng đầu danh sách chờ của buổi học này
        waitingListRepoPort.getFirstInQueue(booking.getSessionId()).ifPresent(firstWait -> {

            // Đổi trạng thái hàng chờ của người đó sang Promoted
            firstWait.setStatus("Promoted");
            waitingListRepoPort.save(firstWait);

            // Tự động tạo một suất Booking mới đặt vào chỗ trống vừa nhả ra
            Booking autoBooking = Booking.builder()
                    .memberId(firstWait.getMemberId())
                    .sessionId(firstWait.getSessionId())
                    .status("Confirmed")
                    .build();
            bookingRepoPort.save(autoBooking);
        });
    }
}