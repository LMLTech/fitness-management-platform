package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.PersonalTrainingSession;
import com.fitness.core.auth.domain.TrainingLog;
import java.util.List;
import java.util.UUID;

public interface ITrainingLogUseCase {
    void recordTrainingSession(UUID sessionId, PersonalTrainingSession sessionInfo, List<TrainingLog> logs);
    List<TrainingLog> getLogsBySession(UUID sessionId);
}