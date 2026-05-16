package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.FreezeApprovalDto;
import com.fitness.api.auth.dto.FreezeRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.SubscriptionFreezeLog;
import com.fitness.core.auth.port.in.ISubscriptionFreezeUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort; // Inject để tìm UUID admin qua Email đăng nhập JWT
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions/freeze")
@RequiredArgsConstructor
public class SubscriptionFreezeController {

    private final ISubscriptionFreezeUseCase freezeUseCase;
    private final IUserRepositoryPort userRepoPort;

    // 1. API dành cho Hội viên tự làm đơn xin bảo lưu trực tuyến trên App di động
    @PostMapping("/request")
    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<ApiResponse<SubscriptionFreezeLog>> memberSubmitRequest(@RequestBody FreezeRequestDto dto) {
        SubscriptionFreezeLog result = freezeUseCase.createFreezeRequest(
                dto.getSubscriptionId(), dto.getReason(), dto.getFreezeStart(), dto.getFreezeEnd()
        );
        return ResponseEntity.ok(ApiResponse.success(result, "Gửi đơn yêu cầu bảo lưu gói tập thành công! Vui lòng chờ lễ tân duyệt."));
    }

    // 2. API dành cho Lễ tân trực quầy hoặc Admin phê duyệt đơn đóng/mở gói tập
    @PostMapping("/approve")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Void>> adminProcessRequest(@RequestBody FreezeApprovalDto dto) {
        // Lấy email của người đang đăng nhập hệ thống từ SecurityContext
        String currentAdminEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Tìm ngược ra UUID User ID của Admin/Lễ tân đó để ghi nhận vào log
        UUID adminId = userRepoPort.findByEmail(currentAdminEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản quản trị viên hiện hành"))
                .getId();

        freezeUseCase.processApproval(dto.getLogId(), adminId, dto.isApproved());

        String msg = dto.isApproved() ? "Đã phê duyệt bảo lưu, gói tập của hội viên đã tạm khóa và cộng bù ngày thành công!" : "Đã từ chối đơn yêu cầu bảo lưu của hội viên.";
        return ResponseEntity.ok(ApiResponse.success(null, msg));
    }
}