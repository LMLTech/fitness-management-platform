package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.GuestVisitRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.CheckInLog;
import com.fitness.core.auth.domain.GuestVisit;
import com.fitness.core.auth.port.in.ICheckInUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operations")
@RequiredArgsConstructor
public class CheckInController {

    private final ICheckInUseCase checkInUseCase;
    private final IUserRepositoryPort userRepoPort;

    // KỊCH BẢN 1: Hội viên tự quét QR hoặc Lễ tân quét thẻ hội viên tại quầy
    @PostMapping("/checkin/member/{memberId}/session/{sessionId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER', 'ROLE_RECEPTIONIST', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CheckInLog>> doMemberCheckIn(
            @PathVariable("memberId") UUID memberId,
            @PathVariable("sessionId") UUID sessionId,
            @RequestParam(value = "method", defaultValue = "QR") String method) {

        // Lấy thông tin tài khoản người đang thao tác trên hệ thống
        String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID operatorId = userRepoPort.findByEmail(currentUserEmail).map(u -> u.getId()).orElse(null);

        CheckInLog log = checkInUseCase.memberCheckIn(memberId, sessionId, method, operatorId);
        return ResponseEntity.ok(ApiResponse.success(log, "Hội viên điểm danh vào lớp học thành công!"));
    }

    // KỊCH BẢN 2: Lễ tân đón tiếp khách vãng lai đến trải nghiệm dịch vụ
    @PostMapping("/checkin/guest")
    @PreAuthorize("hasAnyAuthority('ROLE_RECEPTIONIST', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<GuestVisit>> doGuestCheckIn(@RequestBody GuestVisitRequestDto dto) {
        String receptionistEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID staffId = userRepoPort.findByEmail(receptionistEmail).map(u -> u.getId()).orElse(null);

        GuestVisit visit = checkInUseCase.registerGuestVisit(
                dto.getFullName(), dto.getPhoneNumber(), dto.getVisitType(), dto.getAccompaniedByMemberId(), staffId);
        return ResponseEntity.ok(ApiResponse.success(visit, "Ghi nhận khách dùng thử đến quầy thành công!"));
    }
}