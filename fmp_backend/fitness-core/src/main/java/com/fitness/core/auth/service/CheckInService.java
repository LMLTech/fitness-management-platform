package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Booking;
import com.fitness.core.auth.domain.CheckInLog;
import com.fitness.core.auth.domain.GuestVisit;
import com.fitness.core.auth.port.in.ICheckInUseCase;
import com.fitness.core.auth.port.out.IBookingRepositoryPort;
import com.fitness.core.auth.port.out.ICheckInRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckInService implements ICheckInUseCase {

    private final ICheckInRepositoryPort checkInRepoPort;
    private final IBookingRepositoryPort bookingRepoPort;

    @Override
    @Transactional
    public CheckInLog memberCheckIn(UUID memberUserId, UUID sessionId, String method, UUID staffId) {
        // 1. Kiểm tra xem hội viên này có lịch đặt chỗ cho buổi học này không
        Booking booking = bookingRepoPort.findByMemberIdAndSessionId(memberUserId, sessionId)
                .orElseThrow(() -> new DomainException("BOOKING_NOT_FOUND", "Hội viên chưa đăng ký đặt chỗ suất học này, không thể điểm danh"));

        if ("Attended".equalsIgnoreCase(booking.getStatus())) {
            throw new DomainException("ALREADY_CHECKED_IN", "Hội viên này đã được điểm danh check-in trước đó rồi");
        }

        // 2. Chuyển trạng thái booking sang đã tham gia học
        booking.setStatus("Attended");
        booking.setCheckedInAt(LocalDateTime.now());
        bookingRepoPort.save(booking);

        // 3. Ghi nhận log vào hệ thống
        CheckInLog log = CheckInLog.builder()
                .userId(memberUserId)
                .sessionId(sessionId)
                .accessMethod(method) // QR, Card, hoặc Manual
                .checkInTime(LocalDateTime.now())
                .processedBy(staffId)
                .build();

        return checkInRepoPort.saveLog(log);
    }

    @Override
    @Transactional
    public GuestVisit registerGuestVisit(String fullName, String phoneNumber, String visitType, UUID accompaniedMemberId, UUID staffId) {
        // 1. Tạo nhật ký check-in tổng cho khách vãng lai (userId và sessionId để trống)
        CheckInLog baseLog = CheckInLog.builder()
                .accessMethod("Guest")
                .checkInTime(LocalDateTime.now())
                .processedBy(staffId)
                .build();
        CheckInLog savedLog = checkInRepoPort.saveLog(baseLog);

        // 2. Lưu chi tiết thông tin khách dùng thử liên kết với log check-in vừa tạo
        GuestVisit guestVisit = GuestVisit.builder()
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .checkInLogId(savedLog.getId())
                .visitType(visitType) // Trial hoặc Guest_Of_Member
                .accompaniedByMemberId(accompaniedMemberId)
                .build();

        return checkInRepoPort.saveGuestVisit(guestVisit);
    }
}