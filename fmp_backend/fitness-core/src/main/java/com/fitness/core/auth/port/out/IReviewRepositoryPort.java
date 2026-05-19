package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.TrainerReview;
import java.util.UUID;

public interface IReviewRepositoryPort {
    TrainerReview saveTrainerReview(TrainerReview review);
    boolean hasTrainedWith(UUID memberId, UUID trainerId); // Tránh review ảo
}