package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.TrainerReview;
import java.util.UUID;

public interface IReviewUseCase {
    TrainerReview submitTrainerReview(UUID reviewerId, TrainerReview review);
}