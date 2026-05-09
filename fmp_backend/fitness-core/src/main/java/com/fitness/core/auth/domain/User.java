package com.fitness.core.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String username;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private String fullName;
    private String avatarUrl;
    private String gender;
    private String status;
    private String googleId;
    private Boolean is2faEnabled;
    private String twoFactorSecret;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}