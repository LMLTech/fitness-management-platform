package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.ForumThread;
import com.fitness.core.auth.port.in.IForumUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/forum")
@RequiredArgsConstructor
public class ForumController {

    private final IForumUseCase forumUseCase;
    private final IUserRepositoryPort userRepoPort;

    private UUID getAuthenticatedUserId() {
        String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepoPort.findByEmail(currentEmail).map(u -> u.getId()).orElseThrow();
    }

    @PostMapping("/threads")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ForumThread>> createThread(@RequestBody ForumThread request) {
        UUID authorId = getAuthenticatedUserId();

        // Map từ DTO sang Domain
        ForumThread threadDomain = ForumThread.builder()
                .categoryId(request.getCategoryId())
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        ForumThread created = forumUseCase.createThread(authorId, request);
        return ResponseEntity.ok(ApiResponse.success(created, "Đăng bài viết lên diễn đàn thành công!"));
    }
}