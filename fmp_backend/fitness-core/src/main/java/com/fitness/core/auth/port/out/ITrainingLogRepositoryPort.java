package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.PersonalTrainingSession;
import com.fitness.core.auth.domain.TrainingLog;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITrainingLogRepositoryPort {
    void savePTSession(PersonalTrainingSession session);
    void saveTrainingLogs(List<TrainingLog> logs);
    Optional<PersonalTrainingSession> findPTSessionById(UUID sessionId);
    List<TrainingLog> findLogsBySessionId(UUID sessionId);
}