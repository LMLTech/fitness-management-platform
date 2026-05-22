package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.BookingRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Booking;
import com.fitness.core.auth.port.in.IBookingUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final IBookingUseCase bookingUseCase;
    private final IUserRepositoryPort userRepoPort;

    // API Đặt chỗ dành riêng cho Hội viên đã đăng nhập hệ thống
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<ApiResponse<Booking>> createBooking(@RequestBody BookingRequestDto dto) {
        // Lấy thông tin email từ JWT Token đang đăng nhập
        String currentMemberEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Tìm ra UUID gốc của hội viên
        UUID memberUserId = userRepoPort.findByEmail(currentMemberEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản hội viên hiện hành"))
                .getId();

        Booking booking = bookingUseCase.bookClassSession(memberUserId, dto.getSessionId());
        return ResponseEntity.ok(ApiResponse.success(booking, "Đặt chỗ giữ suất lớp học thành công!"));
    }

    // API Hủy đặt chỗ (Chuẩn bảo mật lấy ID từ Token)
    @PutMapping("/{bookingId}/cancel")
    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER')") // Bắt buộc phải có quyền Hội viên
    public ResponseEntity<ApiResponse<Void>> cancelBooking(@PathVariable UUID bookingId) {

        // 1. Lấy thông tin email từ JWT Token giống hệt như lúc đặt chỗ (Chống Fake ID)
        String currentMemberEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Tìm ra UUID gốc của hội viên đang thực hiện thao tác hủy
        UUID memberUserId = userRepoPort.findByEmail(currentMemberEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản hội viên hiện hành"))
                .getId();

        // 3. Gọi logic hủy lớp và tự động đôn danh sách chờ (Waitlist Promotion)
        bookingUseCase.cancelBooking(bookingId, memberUserId);

        return ResponseEntity.ok(ApiResponse.success(null, "Đã hủy đặt chỗ thành công!"));
    }
}