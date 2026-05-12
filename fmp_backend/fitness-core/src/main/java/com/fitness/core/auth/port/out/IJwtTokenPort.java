package com.fitness.core.auth.port.out;
import java.util.List;
import com.fitness.core.auth.domain.User;

public interface IJwtTokenPort {
    String generateToken(User user);

    String getEmailFromToken(String token);
    boolean validateToken(String token);

    List<String> getRolesFromToken(String token);
}