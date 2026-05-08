package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Member;
import com.fitness.core.auth.domain.User;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepositoryPort {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    User saveUserAndMember(User user, Member member);
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);
    // lưu secret 2FA
    void update2FASecret(UUID userId, String secret);
    // bật 2FA
    void enable2FA(UUID userId);

    Optional<User> findByGoogleId(String googleId);
    void updateSocialId(UUID userId, String provider, String socialId);
}