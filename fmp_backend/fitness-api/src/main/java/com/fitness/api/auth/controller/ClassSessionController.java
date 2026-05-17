package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.CreateSessionRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.ClassSession;
import com.fitness.core.auth.port.in.ICreateClassSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class ClassSessionController {

    private final ICreateClassSessionUseCase createClassSessionUseCase;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ClassSession>> createNewSession(@RequestBody CreateSessionRequestDto dto) {
        ClassSession session = createClassSessionUseCase.scheduleNewSession(
                dto.getClassId(),
                dto.getTrainerId(),
                dto.getBranchId(),
                dto.getRoomId(),
                dto.getDate(),
                dto.getStartTime(),
                dto.getMaxCapacity()
        );
        return ResponseEntity.ok(ApiResponse.success(session, "Xếp lịch dạy và khởi tạo buổi học thành công!"));
    }
}