package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Booking;
import com.fitness.core.auth.domain.ClassSession;
import com.fitness.core.auth.domain.Subscription;
import com.fitness.core.auth.domain.WaitingList;
import com.fitness.core.auth.port.in.IBookingUseCase;
import com.fitness.core.auth.port.out.IBookingRepositoryPort;
import com.fitness.core.auth.port.out.IClassSessionRepositoryPort;
import com.fitness.core.auth.port.out.ISubscriptionRepositoryPort;
import com.fitness.core.auth.port.in.INotificationUseCase;
import com.fitness.core.auth.port.out.IWaitingListRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingUseCase {

    private final IBookingRepositoryPort bookingRepoPort;
    private final IClassSessionRepositoryPort sessionRepoPort;
    private final ISubscriptionRepositoryPort subscriptionRepoPort;
    private final IWaitingListRepositoryPort waitingListRepoPort;
    private final INotificationUseCase notificationUseCase;

    @Override
    @Transactional(noRollbackFor = DomainException.class)
    public Booking bookClassSession(UUID memberUserId, UUID sessionId) {

        ClassSession session = sessionRepoPort.findById(sessionId)
                .orElseThrow(() -> new DomainException("SESSION_NOT_FOUND", "Buổi học này không tồn tại trên hệ thống"));

        if (!"Scheduled".equalsIgnoreCase(session.getStatus())) {
            throw new DomainException("INVALID_SESSION_STATUS", "Buổi học này đã bắt đầu, kết thúc hoặc đã bị hủy");
        }

        List<Subscription> subs = subscriptionRepoPort.findByMemberId(memberUserId);
        boolean hasValidSub = subs.stream().anyMatch(sub ->
                "Active".equalsIgnoreCase(sub.getStatus()) &&
                        !session.getDate().isBefore(sub.getStartDate()) &&
                        !session.getDate().isAfter(sub.getEndDate())
        );

        if (!hasValidSub) {
            throw new DomainException("NO_ACTIVE_SUBSCRIPTION", "Bạn không có gói tập nào đang kích hoạt (hoặc gói tập đã hết hạn/bị đóng băng) tại ngày diễn ra lớp học");
        }

        if (bookingRepoPort.hasMemberBooked(memberUserId, sessionId)) {
            throw new DomainException("ALREADY_BOOKED", "Bạn đã đặt chỗ cho buổi học này rồi");
        }

        long currentBookings = bookingRepoPort.countConfirmedBookings(sessionId);
        if (currentBookings >= session.getMaxCapacity()) {
            if (waitingListRepoPort.isMemberInWaitlist(memberUserId, sessionId)) {
                throw new DomainException("ALREADY_IN_WAITLIST", "Lớp đã đầy và bạn đã nằm trong danh sách chờ của lớp này rồi");
            }
            int nextPosition = waitingListRepoPort.getMaxPosition(sessionId) + 1;
            WaitingList waitEntry = WaitingList.builder()
                    .memberId(memberUserId)
                    .sessionId(sessionId)
                    .position(nextPosition)
                    .status("Waiting")
                    .build();
            waitingListRepoPort.save(waitEntry);
            throw new DomainException("ADDED_TO_WAITLIST", "Lớp học hiện tại đã đầy slot! Hệ thống đã tự động đưa bạn vào danh sách chờ ở vị trí số " + nextPosition);
        }

        Booking booking = Booking.builder()
                .memberId(memberUserId)
                .sessionId(sessionId)
                .status("Confirmed")
                .build();

        Booking savedBooking = bookingRepoPort.save(booking);

        notificationUseCase.createNotification(
                memberUserId,
                "Đặt chỗ thành công 🗓️",
                "Bạn đã giữ chỗ thành công cho buổi tập ngày " + session.getDate().toString() + ". Hãy đến sớm 10 phút nhé!",
                "BOOKING_SUCCESS"
        );

        return savedBooking;
    }

    @Override
    @Transactional
    public void cancelBooking(UUID bookingId, UUID memberUserId) {

        Booking booking = bookingRepoPort.findById(bookingId)
                .orElseThrow(() -> new DomainException("BOOKING_NOT_FOUND", "Không tìm thấy thông tin đặt chỗ"));

        if (!booking.getMemberId().equals(memberUserId)) {
            throw new DomainException("UNAUTHORIZED_ACTION", "Bạn không có quyền hủy lớp của người khác");
        }

        if (!"Confirmed".equalsIgnoreCase(booking.getStatus())) {
            throw new DomainException("INVALID_BOOKING_STATUS", "Chỉ có thể hủy lớp khi đang ở trạng thái Confirmed");
        }

        ClassSession session = sessionRepoPort.findById(booking.getSessionId())
                .orElseThrow(() -> new DomainException("SESSION_NOT_FOUND", "Buổi học này không tồn tại"));

        booking.setStatus("Cancelled");
        bookingRepoPort.save(booking);

        notificationUseCase.createNotification(
                memberUserId,
                "Hủy chỗ thành công ❌",
                "Bạn đã hủy chỗ buổi tập ngày " + session.getDate().toString() + ". Hẹn gặp lại bạn vào buổi sau!",
                "BOOKING_CANCELLED"
        );

        waitingListRepoPort.getFirstInQueue(session.getId()).ifPresent(waitEntry -> {

            waitEntry.setStatus("Promoted");
            waitingListRepoPort.save(waitEntry);

            Booking newBooking = Booking.builder()
                    .memberId(waitEntry.getMemberId())
                    .sessionId(session.getId())
                    .status("Confirmed")
                    .build();
            bookingRepoPort.save(newBooking);

            notificationUseCase.createNotification(
                    waitEntry.getMemberId(),
                    "Tin vui! Bạn đã được xếp lớp 🎉",
                    "Có người vừa hủy chỗ! Hệ thống đã tự động xếp bạn vào danh sách chính thức lớp ngày " + session.getDate().toString() + ". Nhớ đi tập nhé!",
                    "WAITLIST_PROMOTED"
            );
        });
    }
}