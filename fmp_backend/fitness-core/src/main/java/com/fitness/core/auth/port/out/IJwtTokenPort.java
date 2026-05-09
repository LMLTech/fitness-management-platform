package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.User;

public interface IJwtTokenPort {
    String generateToken(User user);

    String getEmailFromToken(String token);
    boolean validateToken(String token);
}