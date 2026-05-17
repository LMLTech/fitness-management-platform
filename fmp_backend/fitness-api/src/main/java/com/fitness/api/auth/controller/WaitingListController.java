package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.port.in.IWaitingListUseCase;
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
public class WaitingListController {

    private final IWaitingListUseCase waitingListUseCase;
    private final IUserRepositoryPort userRepoPort;

    // API Hội viên tự hủy lịch hẹn để nhường slot cho người trong hàng chờ
    @PutMapping("/{bookingId}/cancel")
    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<ApiResponse<Void>> cancelMyBooking(@PathVariable("bookingId") UUID bookingId) {
        String currentMemberEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UUID memberUserId = userRepoPort.findByEmail(currentMemberEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản hội viên hiện hành"))
                .getId();

        waitingListUseCase.cancelBookingAndPromoteNext(memberUserId, bookingId);
        return ResponseEntity.ok(ApiResponse.success(null, "Hủy lịch đặt chỗ thành công! Hệ thống đã tự động nhường slot và đôn người trong danh sách chờ lên."));
    }
}