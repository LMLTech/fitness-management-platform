package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Member;
import com.fitness.core.auth.domain.User;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import com.fitness.infrastructure.auth.entity.MemberJpaEntity;
import com.fitness.infrastructure.auth.entity.UserJpaEntity;
import com.fitness.infrastructure.auth.repository.MemberJpaRepository;
import com.fitness.infrastructure.auth.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMysqlAdapter implements IUserRepositoryPort {

    private final UserJpaRepository userRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public boolean existsByEmail(String email) { return userRepository.existsByEmail(email); }

    @Override
    public boolean existsByUsername(String username) { return userRepository.existsByUsername(username); }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) { return userRepository.existsByPhoneNumber(phoneNumber); }

    @Override
    @Transactional
    public User saveUserAndMember(User user, Member member) {
        // 1. Map Domain User -> JPA Entity
        UserJpaEntity userEntity = UserJpaEntity.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .passwordHash(user.getPasswordHash())
                .fullName(user.getFullName())
                .status(user.getStatus())
                .is2faEnabled(false)
                .twoFactorSecret(null)
                .build();

        // 2. Lưu User và ép Hibernate đẩy dữ liệu xuống DB ngay lập tức
        UserJpaEntity savedUser = userRepository.saveAndFlush(userEntity);

        // 3. Map Domain Member -> JPA Entity và lưu
        MemberJpaEntity memberEntity = MemberJpaEntity.builder()
                .user(savedUser)
                .memberCode(member.getMemberCode())
                .referralCode(member.getReferralCode())
                .build();
        memberRepository.save(memberEntity);

        // 4. Map ngược từ "savedUser" (đã có ID và createdAt) sang Domain User
        return mapToDomain(savedUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToDomain);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId)
                .map(this::mapToDomain);
    }

    @Override
    @Transactional
    public void update2FASecret(UUID userId, String secret) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setTwoFactorSecret(secret);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void enable2FA(UUID userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setIs2faEnabled(true);
            userRepository.save(user);
        });
    }

    // Helper method để map dữ liệu đồng nhất
    private User mapToDomain(UserJpaEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .passwordHash(entity.getPasswordHash())
                .fullName(entity.getFullName())
                .status(entity.getStatus())
                .is2faEnabled(entity.getIs2faEnabled())
                .twoFactorSecret(entity.getTwoFactorSecret())
                .createdAt(entity.getCreatedAt()) // Lấy giá trị đã được DB sinh ra
                .build();
    }
}