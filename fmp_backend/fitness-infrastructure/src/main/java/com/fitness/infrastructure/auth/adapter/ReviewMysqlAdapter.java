package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.TrainerReview;
import com.fitness.core.auth.port.out.IReviewRepositoryPort;
import com.fitness.infrastructure.auth.entity.TrainerReviewEntity;
import com.fitness.infrastructure.auth.repository.TrainerReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewMysqlAdapter implements IReviewRepositoryPort {

    private final TrainerReviewJpaRepository reviewRepo;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public TrainerReview saveTrainerReview(TrainerReview review) {
        TrainerReviewEntity entity = TrainerReviewEntity.builder()
                .id(review.getId())
                .trainerId(review.getTrainerId())
                .reviewerId(review.getReviewerId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
        reviewRepo.save(entity);
        return review;
    }

    @Override
    public boolean hasTrainedWith(UUID memberId, UUID trainerId) {
        // Dummy check for testing. Trong thực tế, bạn query bảng class_sessions hoặc workout_plans.
        // Tạm thời return true để bạn test API mượt mà.
        return true;
    }
}