package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResult {
    private User user;
    private Member member;
}