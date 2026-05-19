package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.PersonalTrainingSession;
import com.fitness.core.auth.domain.TrainingLog;
import com.fitness.core.auth.port.in.ITrainingLogUseCase;
import com.fitness.core.auth.port.out.ITrainingLogRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingLogService implements ITrainingLogUseCase {

    private final ITrainingLogRepositoryPort logRepositoryPort;

    @Override
    @Transactional
    public void recordTrainingSession(UUID sessionId, PersonalTrainingSession sessionInfo, List<TrainingLog> logs) {
        // 1. Chặn đứng dữ liệu rác hoặc khuyết bài tập nhật ký
        if (logs == null || logs.isEmpty()) {
            throw new DomainException("EMPTY_LOGS", "Nhat ky buoi tap bat buoc phai co it nhat mot ghi nhan bai tap");
        }

        // 2. Kiểm tra tính hợp lệ của các chỉ số thể hình thực tế
        for (TrainingLog log : logs) {
            if (log.getSets() <= 0 || log.getReps() <= 0 || log.getWeight() < 0) {
                throw new DomainException("INVALID_METRICS",
                        "Cac chi so sets, reps hoac weight cua bai tap [" + log.getExerciseName() + "] phai lon hon 0");
            }
        }

        // 3. Đúc ID và gán liên kết cho phiên tập 1-1
        sessionInfo.setSessionId(sessionId);
        logRepositoryPort.savePTSession(sessionInfo);

        // 4. Chuẩn hóa và đúc mã UUID cho từng dòng nhật ký chi tiết
        logs.forEach(log -> {
            log.setId(UUID.randomUUID());
            log.setPtSessionId(sessionId);
        });
        logRepositoryPort.saveTrainingLogs(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingLog> getLogsBySession(UUID sessionId) {
        // Kiểm tra xem phiên tập này có tồn tại không
        logRepositoryPort.findPTSessionById(sessionId)
                .orElseThrow(() -> new DomainException("SESSION_NOT_FOUND", "Phien tap 1-1 nay chua duoc ghi nhan nhat ky"));

        return logRepositoryPort.findLogsBySessionId(sessionId);
    }
}