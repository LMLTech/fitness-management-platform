package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.TrainerReview;
import com.fitness.core.auth.port.in.IReviewUseCase;
import com.fitness.core.auth.port.out.IReviewRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewUseCase {

    private final IReviewRepositoryPort reviewRepoPort;

    @Override
    @Transactional
    public TrainerReview submitTrainerReview(UUID reviewerId, TrainerReview review) {
        // 1. Validate điểm sao
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new DomainException("INVALID_RATING", "Điểm đánh giá phải từ 1 đến 5 sao");
        }

        // 2. Logic chống Spam: Chỉ hội viên đã từng tập với PT mới được phép review
        if (!reviewRepoPort.hasTrainedWith(reviewerId, review.getTrainerId())) {
            throw new DomainException("UNAUTHORIZED_REVIEW", "Bạn chỉ được đánh giá HLV khi đã từng tham gia lớp hoặc mua giáo án của họ");
        }

        review.setId(UUID.randomUUID());
        review.setReviewerId(reviewerId);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepoPort.saveTrainerReview(review);
    }
}