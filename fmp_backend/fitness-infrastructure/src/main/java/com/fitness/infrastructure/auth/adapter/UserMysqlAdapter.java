package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Member;
import com.fitness.core.auth.domain.User;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import com.fitness.infrastructure.auth.entity.MemberJpaEntity;
import com.fitness.infrastructure.auth.entity.UserJpaEntity;
import com.fitness.infrastructure.auth.entity.RoleJpaEntity;
import com.fitness.infrastructure.auth.repository.MemberJpaRepository;
import com.fitness.infrastructure.auth.repository.UserJpaRepository;
import com.fitness.infrastructure.auth.repository.RoleJpaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Optional;
import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMysqlAdapter implements IUserRepositoryPort {

    private final UserJpaRepository userRepository;
    private final MemberJpaRepository memberRepository;
    private final RoleJpaRepository roleRepository;

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
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .status(user.getStatus())
                .is2faEnabled(false)
                .googleId(user.getGoogleId())
                .build();
        // Save user trước
        UserJpaEntity savedUser = userRepository.saveAndFlush(userEntity);

        // Save member gắn với user
        MemberJpaEntity memberEntity = MemberJpaEntity.builder()
                .user(savedUser)
                .memberCode(member.getMemberCode())
                .referralCode(member.getReferralCode())
                .build();
        memberRepository.save(memberEntity);

        return mapToDomain(savedUser);
    }
    // hàm lưu User mà không liên quan gì đến bảng Member
    @Override
    @Transactional
    public User saveUserOnly(User user) {
        UserJpaEntity userEntity = UserJpaEntity.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .passwordHash(user.getPasswordHash())
                .fullName(user.getFullName())
                .status(user.getStatus())
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .is2faEnabled(false)
                .build();
        // Dùng saveAndFlush thay vì save
        UserJpaEntity saved = userRepository.saveAndFlush(userEntity);
        return mapToDomain(saved);
    }

    // hàm gán quyền
    @Override
    @Transactional
    public void assignRoleToUser(UUID userId, String roleName) {
        // Tìm Role trong bảng roles
        RoleJpaEntity role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role " + roleName + " không tồn tại"));

        // Tìm User
        UserJpaEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Kiểm tra túi roles, nếu null thì tạo mới
        if (user.getRoles() == null) {
            user.setRoles(new java.util.HashSet<>());
        }
        // Gán role vào set roles của user JPA sẽ tự insert vào bảng user_roles
        user.getRoles().add(role);
        userRepository.save(user);
    }

    // READ METHODS
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::mapToDomain);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId).map(this::mapToDomain);
    }

    @Override
    public Optional<User> findByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId).map(this::mapToDomain);
    }

    // 2FA METHODS
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

    // SOCIAL LOGIN UPDATE
    @Override
    @Transactional
    public void updateSocialId(UUID userId, String provider, String socialId) {
        userRepository.findById(userId).ifPresent(userEntity -> {
            if ("GOOGLE".equalsIgnoreCase(provider)) {
                userEntity.setGoogleId(socialId);
            }
            userRepository.save(userEntity);
        });
    }

    // PROFILE UPDATE
    @Override
    @Transactional
    public void updateProfile(User user) {
        userRepository.findById(user.getId()).ifPresent(userEntity -> {
            userEntity.setFullName(user.getFullName());
            userEntity.setPhoneNumber(user.getPhoneNumber());
            userEntity.setUsername(user.getUsername());
            userEntity.setAvatarUrl(user.getAvatarUrl());
            userEntity.setGender(user.getGender());

            userRepository.save(userEntity);
        });
    }

    // PASSWORD UPDATE
    @Override
    @Transactional
    public void updatePassword(UUID id, String hash) {
        userRepository.findById(id).ifPresent(userEntity -> {
            userEntity.setPasswordHash(hash);
            userRepository.save(userEntity);
        });
    }

    // MAPPER: JPA -> DOMAIN
    private User mapToDomain(UserJpaEntity entity) {
        // Lấy danh sách role từ Set<RoleJpaEntity> -> Set<String>
        Set<String> roleNames = entity.getRoles() != null
                ? entity.getRoles()
                  .stream()
                  .map(RoleJpaEntity::getName)
                  .collect(Collectors.toSet())
                : Collections.emptySet();

        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .passwordHash(entity.getPasswordHash())
                .fullName(entity.getFullName())
                .avatarUrl(entity.getAvatarUrl())
                .gender(entity.getGender())
                .status(entity.getStatus())
                .roles(roleNames) // GÁN ROLE
                .is2faEnabled(entity.getIs2faEnabled())
                .twoFactorSecret(entity.getTwoFactorSecret())
                .googleId(entity.getGoogleId())
                .createdAt(entity.getCreatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}