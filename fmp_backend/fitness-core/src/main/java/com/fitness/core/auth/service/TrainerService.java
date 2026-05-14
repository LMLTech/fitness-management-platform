package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Specialty;
import com.fitness.core.auth.domain.TrainerProfile;
import com.fitness.core.auth.port.in.ITrainerUseCase;
import com.fitness.core.auth.port.out.ITrainerRepositoryPort;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerService implements ITrainerUseCase {

    private final ITrainerRepositoryPort trainerRepositoryPort;
    private final IUserRepositoryPort userRepositoryPort; // Inject để kiểm tra User tồn tại

    @Override
    @Transactional
    public TrainerProfile updateTrainerProfile(UUID userId, TrainerProfile profile, List<String> specialtyNames) {
        // Kiểm tra User có tồn tại không trước khi tạo Profile
        if (!userRepositoryPort.findById(userId).isPresent()) {
            throw new DomainException("USER_NOT_FOUND", "Người dùng không tồn tại để cập nhật hồ sơ PT");
        }

        // Xử lý danh sách chuyên môn
        Set<Specialty> specialties = new HashSet<>();
        if (specialtyNames != null) {
            for (String name : specialtyNames) {
                Specialty specialty = trainerRepositoryPort.findSpecialtyByName(name)
                        .orElseGet(() -> trainerRepositoryPort.saveSpecialty(new Specialty(null, name)));
                specialties.add(specialty);
            }
        }

        // Gán dữ liệu vào Profile
        profile.setUserId(userId);
        profile.setSpecialties(specialties);

        // Lưu vào Database
        trainerRepositoryPort.saveTrainerProfile(profile);

        return profile;
    }

    @Override
    public TrainerProfile getTrainerProfile(UUID userId) {
        return trainerRepositoryPort.findTrainerProfileById(userId)
                .orElseThrow(() -> new DomainException("TRAINER_NOT_FOUND", "Không tìm thấy hồ sơ chuyên sâu của PT này"));
    }
}