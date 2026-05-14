package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Specialty;
import com.fitness.core.auth.domain.TrainerProfile;
import com.fitness.core.auth.port.out.ITrainerRepositoryPort;
import com.fitness.infrastructure.auth.entity.SpecialtyJpaEntity;
import com.fitness.infrastructure.auth.entity.TrainerJpaEntity;
import com.fitness.infrastructure.auth.repository.SpecialtyJpaRepository;
import com.fitness.infrastructure.auth.repository.TrainerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrainerMysqlAdapter implements ITrainerRepositoryPort {

    private final TrainerJpaRepository trainerRepository;
    private final SpecialtyJpaRepository specialtyRepository;

    @Override
    public void saveTrainerProfile(TrainerProfile profile) {
        TrainerJpaEntity entity = TrainerJpaEntity.builder()
                .userId(profile.getUserId())
                .bio(profile.getBio())
                .certifications(profile.getCertifications())
                .commissionRate(profile.getCommissionRate())
                .specialties(profile.getSpecialties().stream()
                        .map(s -> specialtyRepository.findById(s.getId()).orElse(null))
                        .collect(Collectors.toSet()))
                .build();
        trainerRepository.save(entity);
    }

    @Override
    public Optional<Specialty> findSpecialtyByName(String name) {
        return specialtyRepository.findByName(name)
                .map(entity -> new Specialty(entity.getId(), entity.getName()));
    }

    @Override
    public Specialty saveSpecialty(Specialty specialty) {
        SpecialtyJpaEntity entity = SpecialtyJpaEntity.builder()
                .name(specialty.getName())
                .build();
        SpecialtyJpaEntity saved = specialtyRepository.save(entity);
        return new Specialty(saved.getId(), saved.getName());
    }

    @Override
    public Optional<TrainerProfile> findTrainerProfileById(UUID userId) {
        return trainerRepository.findById(userId).map(entity -> TrainerProfile.builder()
                .userId(entity.getUserId())
                .bio(entity.getBio())
                .certifications(entity.getCertifications())
                .commissionRate(entity.getCommissionRate())
                .specialties(entity.getSpecialties().stream()
                        .map(s -> new Specialty(s.getId(), s.getName()))
                        .collect(Collectors.toSet()))
                .build());
    }
}