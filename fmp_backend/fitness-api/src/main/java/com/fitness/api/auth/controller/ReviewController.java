package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.TrainerReview;
import com.fitness.core.auth.port.in.IReviewUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewUseCase reviewUseCase;
    private final IUserRepositoryPort userRepoPort;

    private UUID getAuthenticatedUserId() {
        String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepoPort.findByEmail(currentEmail).map(u -> u.getId()).orElseThrow();
    }

    @PostMapping("/trainers")
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public ResponseEntity<ApiResponse<TrainerReview>> submitTrainerReview(@RequestBody TrainerReview request) {
        UUID reviewerId = getAuthenticatedUserId();

        // Map từ DTO sang Domain
        TrainerReview reviewDomain = TrainerReview.builder()
                .trainerId(request.getTrainerId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        TrainerReview savedReview = reviewUseCase.submitTrainerReview(reviewerId, request);
        return ResponseEntity.ok(ApiResponse.success(savedReview, "Cảm ơn bạn đã gửi đánh giá HLV!"));
    }
}