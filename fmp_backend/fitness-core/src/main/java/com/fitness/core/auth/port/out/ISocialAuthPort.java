package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.User;
import java.util.Optional;

public interface ISocialAuthPort {
    Optional<User> verifyGoogleToken(String idToken);
}
