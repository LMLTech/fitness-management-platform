package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByGoogleId(String googleId);
}