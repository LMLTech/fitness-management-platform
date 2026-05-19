package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.PersonalTrainingSession;
import com.fitness.core.auth.domain.TrainingLog;
import com.fitness.core.auth.port.out.ITrainingLogRepositoryPort;
import com.fitness.infrastructure.auth.entity.PersonalTrainingSessionEntity;
import com.fitness.infrastructure.auth.entity.TrainingLogEntity;
import com.fitness.infrastructure.auth.repository.PersonalTrainingSessionJpaRepository;
import com.fitness.infrastructure.auth.repository.TrainingLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrainingLogMysqlAdapter implements ITrainingLogRepositoryPort {

    private final PersonalTrainingSessionJpaRepository sessionJpaRepo;
    private final TrainingLogJpaRepository logJpaRepo;

    @Override
    public void savePTSession(PersonalTrainingSession session) {
        PersonalTrainingSessionEntity entity = PersonalTrainingSessionEntity.builder()
                .sessionId(session.getSessionId())
                .memberId(session.getMemberId())
                .objectives(session.getObjectives())
                .build();
        sessionJpaRepo.save(entity);
    }

    @Override
    public void saveTrainingLogs(List<TrainingLog> logs) {
        List<TrainingLogEntity> entities = logs.stream()
                .map(log -> TrainingLogEntity.builder()
                        .id(log.getId())
                        .ptSessionId(log.getPtSessionId())
                        .exerciseName(log.getExerciseName())
                        .sets(log.getSets())
                        .reps(log.getReps())
                        .weight(log.getWeight())
                        .notes(log.getNotes())
                        .build())
                .collect(Collectors.toList());
        logJpaRepo.saveAll(entities);
    }

    @Override
    public Optional<PersonalTrainingSession> findPTSessionById(UUID sessionId) {
        return sessionJpaRepo.findById(sessionId)
                .map(entity -> PersonalTrainingSession.builder()
                        .sessionId(entity.getSessionId())
                        .memberId(entity.getMemberId())
                        .objectives(entity.getObjectives())
                        .build());
    }

    @Override
    public List<TrainingLog> findLogsBySessionId(UUID sessionId) {
        return logJpaRepo.findByPtSessionId(sessionId).stream()
                .map(entity -> TrainingLog.builder()
                        .id(entity.getId())
                        .ptSessionId(entity.getPtSessionId())
                        .exerciseName(entity.getExerciseName())
                        .sets(entity.getSets())
                        .reps(entity.getReps())
                        .weight(entity.getWeight())
                        .notes(entity.getNotes())
                        .build())
                .collect(Collectors.toList());
    }
}