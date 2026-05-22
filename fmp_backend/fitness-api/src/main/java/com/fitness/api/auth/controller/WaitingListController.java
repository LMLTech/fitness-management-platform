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
@RequestMapping("/api/v1/waiting-list")
@RequiredArgsConstructor
public class WaitingListController {

    private final IWaitingListUseCase waitingListUseCase;
    private final IUserRepositoryPort userRepoPort;

    // 1. API Xem vị trí trong hàng chờ
    @GetMapping("/sessions/{sessionId}/my-position")
    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<ApiResponse<Integer>> getMyPosition(@PathVariable UUID sessionId) {
        String currentMemberEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID memberUserId = userRepoPort.findByEmail(currentMemberEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản")).getId();

        int position = waitingListUseCase.getMyWaitlistPosition(memberUserId, sessionId);

        String message = position > 0 ? "Vị trí của bạn là số " + position : "Bạn không nằm trong danh sách chờ lớp này";
        return ResponseEntity.ok(ApiResponse.success(position, message));
    }

    // 2. API Chủ động rút khỏi hàng chờ
    @DeleteMapping("/sessions/{sessionId}/leave")
    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<ApiResponse<Void>> leaveWaitlist(@PathVariable UUID sessionId) {
        String currentMemberEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID memberUserId = userRepoPort.findByEmail(currentMemberEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản")).getId();

        waitingListUseCase.leaveWaitlist(memberUserId, sessionId);

        return ResponseEntity.ok(ApiResponse.success(null, "Bạn đã rút khỏi danh sách chờ thành công."));
    }
}