package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.port.in.IChallengeUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final IChallengeUseCase challengeUseCase;
    private final IUserRepositoryPort userRepoPort;

    private UUID getAuthenticatedUserId() {
        String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepoPort.findByEmail(currentEmail)
                .map(u -> u.getId())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Tài khoản không hợp lệ"));
    }

    // Hội viên bấm nút tham gia thử thách
    @PostMapping("/{id}/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> join(@PathVariable UUID id) {
        UUID memberId = getAuthenticatedUserId();
        challengeUseCase.joinChallenge(memberId, id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tham gia thử thách thành công!"));
    }

    // Hệ thống/Admin kích hoạt duyệt hoàn thành thử thách để phát thưởng
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> complete(@PathVariable UUID id, @RequestParam UUID memberId) {
        challengeUseCase.completeChallenge(memberId, id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xác nhận hoàn thành và phát thưởng thành công!"));
    }
}