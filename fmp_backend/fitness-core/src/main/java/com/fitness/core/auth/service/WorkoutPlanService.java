package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.WorkoutPlan;
import com.fitness.core.auth.domain.WorkoutPlanExercise;
import com.fitness.core.auth.domain.TrainerProfile; // Import từ Flow 8
import com.fitness.core.auth.port.in.IWorkoutPlanUseCase;
import com.fitness.core.auth.port.in.ITrainerUseCase; // Import UseCase của Trainer để lấy chuyên môn
import com.fitness.core.auth.port.out.IWorkoutPlanRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService implements IWorkoutPlanUseCase {

    private final IWorkoutPlanRepositoryPort planRepoPort;
    private final ITrainerUseCase trainerUseCase; // Tiêm UseCase Trainer vào để kiểm tra hồ sơ

    @Override
    @Transactional
    public WorkoutPlan createWorkoutPlan(UUID trainerId, WorkoutPlan workoutPlan) {

        // 1. Ràng buộc logic thời gian
        if (workoutPlan.getStartDate().isAfter(workoutPlan.getEndDate())) {
            throw new DomainException("INVALID_PLAN_DATES", "Ngày bắt đầu không thể sau ngày kết thúc giáo án");
        }

        // 2. Kiểm tra giáo án phải có ít nhất một bài tập
        if (workoutPlan.getExercises() == null || workoutPlan.getExercises().isEmpty()) {
            throw new DomainException("EMPTY_EXERCISES", "Giáo án bắt buộc phải có ít nhất một bài tập chi tiết");
        }

        // 3. Kiểm tra chuyên môn của PT (kết nối Flow 8 và Flow 25)
        // Lấy hồ sơ đầy đủ bao gồm cả danh sách chuyên môn của PT đang đăng nhập
        TrainerProfile trainerProfile = trainerUseCase.getTrainerProfile(trainerId);

        // Trích xuất danh sách tên chuyên môn thành chữ HOA (VD: ["GYM", "BOXING"])
        Set<String> certifiedSpecialties = trainerProfile.getSpecialties().stream()
                .map(s -> s.getName().toUpperCase())
                .collect(Collectors.toSet());

        // Kiểm tra từng bài tập có phù hợp với chuyên môn của PT hay không
        for (WorkoutPlanExercise ex : workoutPlan.getExercises()) {
            String exerciseNameUpper = ex.getExerciseName().toUpperCase();

            // Nhận diện bài tập GYM
            if ((exerciseNameUpper.contains("SQUAT")
                    || exerciseNameUpper.contains("PRESS")
                    || exerciseNameUpper.contains("DEADLIFT"))
                    && !certifiedSpecialties.contains("GYM")) {

                throw new DomainException(
                        "UNAUTHORIZED_SPECIALTY",
                        "Bạn không thể thêm bài tập [" + ex.getExerciseName()
                                + "] vì bạn không có chuyên môn GYM!"
                );
            }

            // Nhận diện bài tập BOXING
            if ((exerciseNameUpper.contains("PUNCH")
                    || exerciseNameUpper.contains("KICK")
                    || exerciseNameUpper.contains("HOOK"))
                    && !certifiedSpecialties.contains("BOXING")) {

                throw new DomainException(
                        "UNAUTHORIZED_SPECIALTY",
                        "Bạn không thể thêm bài tập [" + ex.getExerciseName()
                                + "] vì bạn không có chuyên môn BOXING!"
                );
            }

            // Nhận diện bài tập YOGA
            if ((exerciseNameUpper.contains("POSE")
                    || exerciseNameUpper.contains("STRETCH")
                    || exerciseNameUpper.contains("ASANA"))
                    && !certifiedSpecialties.contains("YOGA")) {

                throw new DomainException(
                        "UNAUTHORIZED_SPECIALTY",
                        "Bạn không thể thêm bài tập [" + ex.getExerciseName()
                                + "] vì bạn không có chuyên môn YOGA!"
                );
            }
        }

        // 4. Sinh ID và lưu dữ liệu sau khi vượt qua kiểm tra
        UUID planId = UUID.randomUUID();
        workoutPlan.setId(planId);
        workoutPlan.setTrainerId(trainerId);
        workoutPlan.setCreatedAt(LocalDateTime.now());

        // Gán ID cho từng bài tập con và chuẩn hóa dayOfWeek
        workoutPlan.getExercises().forEach(ex -> {
            ex.setId(UUID.randomUUID());
            ex.setPlanId(planId);

            if (ex.getDayOfWeek() != null) {
                ex.setDayOfWeek(ex.getDayOfWeek().toUpperCase());
            }
        });

        // Lưu giáo án và danh sách bài tập
        WorkoutPlan savedPlan = planRepoPort.savePlan(workoutPlan);
        planRepoPort.saveExercises(workoutPlan.getExercises());

        return savedPlan;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutPlan> getMemberPlans(UUID memberId) {
        // Lấy danh sách giáo án của member
        return planRepoPort.findByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutPlan getPlanDetails(UUID planId) {

        // Tìm giáo án theo ID
        WorkoutPlan plan = planRepoPort.findById(planId)
                .orElseThrow(() -> new DomainException(
                        "PLAN_NOT_FOUND",
                        "Giáo án lộ trình không tồn tại trên hệ thống"
                ));

        // Gắn danh sách bài tập vào giáo án
        plan.setExercises(planRepoPort.findExercisesByPlanId(planId));

        return plan;
    }
}