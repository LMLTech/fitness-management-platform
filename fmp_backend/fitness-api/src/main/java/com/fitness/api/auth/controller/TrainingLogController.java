package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.RecordTrainingLogDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.PersonalTrainingSession;
import com.fitness.core.auth.domain.TrainingLog;
import com.fitness.core.auth.port.in.ITrainingLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/training-logs")
@RequiredArgsConstructor
public class TrainingLogController {

    private final ITrainingLogUseCase trainingLogUseCase;

    // API PT ghi nhật ký buổi tập 1-1
    @PostMapping("/sessions/{sessionId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TRAINER')")
    public ResponseEntity<ApiResponse<String>> recordLogs(
            @PathVariable UUID sessionId,
            @RequestBody RecordTrainingLogDto dto) {

        // Tạo thông tin buổi tập cá nhân
        PersonalTrainingSession sessionInfo = PersonalTrainingSession.builder()
                .memberId(dto.getMemberId())
                .objectives(dto.getObjectives())
                .build();

        // Chuyển danh sách log từ DTO sang domain
        List<TrainingLog> domainLogs = dto.getLogs().stream()
                .map(item -> TrainingLog.builder()
                        .exerciseName(item.getExerciseName())
                        .sets(item.getSets())
                        .reps(item.getReps())
                        .weight(item.getWeight())
                        .notes(item.getNotes())
                        .build())
                .collect(Collectors.toList());

        // Gọi use case để lưu nhật ký buổi tập
        trainingLogUseCase.recordTrainingSession(sessionId, sessionInfo, domainLogs);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Ghi nhật ký và đánh giá buổi tập 1-1 thành công!"
                )
        );
    }

    // API xem nhật ký chi tiết của một buổi tập
    @GetMapping("/sessions/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TrainingLog>>> getSessionLogs(
            @PathVariable UUID sessionId) {

        // Lấy danh sách nhật ký theo session
        List<TrainingLog> logs = trainingLogUseCase.getLogsBySession(sessionId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        logs,
                        "Tải nhật ký chi tiết buổi tập thành công!"
                )
        );
    }
}