package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Booking;
import com.fitness.core.auth.domain.ClassSession;
import com.fitness.core.auth.domain.Subscription;
import com.fitness.core.auth.port.in.IBookingUseCase;
import com.fitness.core.auth.port.out.IBookingRepositoryPort;
import com.fitness.core.auth.port.out.IClassSessionRepositoryPort;
import com.fitness.core.auth.port.out.ISubscriptionRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingUseCase {

    private final IBookingRepositoryPort bookingRepoPort;
    private final IClassSessionRepositoryPort sessionRepoPort;
    private final ISubscriptionRepositoryPort subscriptionRepoPort;

    @Override
    @Transactional
    public Booking bookClassSession(UUID memberUserId, UUID sessionId) {

        // 1. Kiểm tra buổi học có tồn tại không
        ClassSession session = sessionRepoPort.findById(sessionId)
                .orElseThrow(() -> new DomainException("SESSION_NOT_FOUND", "Buổi học này không tồn tại trên hệ thống"));

        // 2. Chỉ cho phép đặt chỗ nếu lớp ở trạng thái 'Scheduled'
        if (!"Scheduled".equalsIgnoreCase(session.getStatus())) {
            throw new DomainException("INVALID_SESSION_STATUS", "Buổi học này đã bắt đầu, kết thúc hoặc đã bị hủy");
        }

        // 3. KIỂM TRA GÓI TẬP: Hội viên phải có gói tập đang Active
        List<Subscription> subs = subscriptionRepoPort.findByMemberId(memberUserId);
        boolean hasValidSub = subs.stream().anyMatch(sub ->
                "Active".equalsIgnoreCase(sub.getStatus()) &&
                        !session.getDate().isBefore(sub.getStartDate()) &&
                        !session.getDate().isAfter(sub.getEndDate())
        );

        if (!hasValidSub) {
            throw new DomainException("NO_ACTIVE_SUBSCRIPTION", "Bạn không có gói tập nào đang kích hoạt (hoặc gói tập đã hết hạn/bị đóng băng) tại ngày diễn ra lớp học");
        }

        // 4. KIỂM TRA TRÙNG LẶP: Hội viên đã đặt lớp này chưa
        if (bookingRepoPort.hasMemberBooked(memberUserId, sessionId)) {
            throw new DomainException("ALREADY_BOOKED", "Bạn đã đặt chỗ cho buổi học này rồi");
        }

        // 5. KIỂM TRA SỨC CHỨA: Đếm số lượng slot hiện tại
        long currentBookings = bookingRepoPort.countConfirmedBookings(sessionId);
        if (currentBookings >= session.getMaxCapacity()) {
            // Tạm thời báo lỗi đầy lớp flow 19 sẽ nâng cấp đoạn này lên thành đưa vào danh sách chờ Waitlist
            throw new DomainException("CLASS_FULL", "Lớp học đã đạt số lượng học viên tối đa, không thể đặt thêm chỗ");
        }

        // 6. Lưu thông tin đặt chỗ thành công
        Booking booking = Booking.builder()
                .memberId(memberUserId)
                .sessionId(sessionId)
                .status("Confirmed")
                .build();

        return bookingRepoPort.save(booking);
    }
}