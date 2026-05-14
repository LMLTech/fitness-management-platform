package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.TrainerProfile;
import java.util.UUID;
import java.util.List;

public interface ITrainerUseCase {
    // Cập nhật hồ sơ PT bao gồm bio, bằng cấp và danh sách chuyên môn
    TrainerProfile updateTrainerProfile(UUID userId, TrainerProfile profile, List<String> specialtyNames);

    // Lấy hồ sơ chi tiết PT
    TrainerProfile getTrainerProfile(UUID userId);
}