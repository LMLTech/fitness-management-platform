package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Member;
import com.fitness.core.auth.domain.User;

import java.util.Optional;

public interface IUserRepositoryPort {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    User saveUserAndMember(User user, Member member);
    Optional<User> findByEmail(String email);
}