package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.SpecialtyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SpecialtyJpaRepository extends JpaRepository<SpecialtyJpaEntity, Integer> {
    Optional<SpecialtyJpaEntity> findByName(String name);
}