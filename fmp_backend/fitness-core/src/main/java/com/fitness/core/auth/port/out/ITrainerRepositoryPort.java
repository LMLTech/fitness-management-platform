package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Specialty;
import com.fitness.core.auth.domain.TrainerProfile;
import java.util.Optional;
import java.util.UUID;

public interface ITrainerRepositoryPort {
    // Lưu thông tin chuyên sâu của PT
    void saveTrainerProfile(TrainerProfile profile);

    // Tìm kiếm chuyên môn theo tên để tránh tạo trùng
    Optional<Specialty> findSpecialtyByName(String name);

    // Lưu một chuyên môn mới vào danh mục
    Specialty saveSpecialty(Specialty specialty);

    // Lấy thông tin PT kèm chuyên môn
    Optional<TrainerProfile> findTrainerProfileById(UUID userId);
}