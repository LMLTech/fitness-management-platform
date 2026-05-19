package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.CreateWorkoutPlanDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.WorkoutPlan;
import com.fitness.core.auth.domain.WorkoutPlanExercise;
import com.fitness.core.auth.port.in.IWorkoutPlanUseCase;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final IWorkoutPlanUseCase workoutPlanUseCase;
    private final IUserRepositoryPort userRepoPort;

    private UUID getAuthenticatedUserId() {
        String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepoPort.findByEmail(currentEmail)
                .map(u -> u.getId())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Tai khoan khong hop le"));
    }

    // API DÀNH RIÊNG PT: Tạo giáo án lộ trình cho học viên
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TRAINER')")
    public ResponseEntity<ApiResponse<WorkoutPlan>> createPlan(@RequestBody CreateWorkoutPlanDto dto) {
        UUID trainerId = getAuthenticatedUserId();

        // Map DTO sang Domain nguyên bản
        List<WorkoutPlanExercise> coreExercises = dto.getExercises().stream()
                .map(ex -> WorkoutPlanExercise.builder()
                        .exerciseName(ex.getExerciseName())
                        .targetSets(ex.getTargetSets())
                        .targetReps(ex.getTargetReps())
                        .dayOfWeek(ex.getDayOfWeek())
                        .build())
                .collect(Collectors.toList());

        WorkoutPlan domainPlan = WorkoutPlan.builder()
                .memberId(dto.getMemberId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .exercises(coreExercises)
                .build();

        WorkoutPlan savedPlan = workoutPlanUseCase.createWorkoutPlan(trainerId, domainPlan);
        return ResponseEntity.ok(ApiResponse.success(savedPlan, "Tao giao an lo trinh tap luyen cho hoc vien thanh cong!"));
    }

    //  API DÀNH CHO HỘI VIÊN: Xem toàn bộ danh sách lộ trình của mình
    @GetMapping("/my-plans")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<WorkoutPlan>>> getMyPlans() {
        UUID memberId = getAuthenticatedUserId();
        List<WorkoutPlan> plans = workoutPlanUseCase.getMemberPlans(memberId);
        return ResponseEntity.ok(ApiResponse.success(plans, "Tai danh sach lo trinh tap luyen thanh cong!"));
    }

    //  API XEM CHI TIẾT GIÁO ÁN (Bao gồm cả danh sách bài tập)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<WorkoutPlan>> getPlanDetails(@PathVariable("id") UUID id) {
        WorkoutPlan plan = workoutPlanUseCase.getPlanDetails(id);
        return ResponseEntity.ok(ApiResponse.success(plan, "Tai chi tiet giao an tap luyen thanh cong!"));
    }
}