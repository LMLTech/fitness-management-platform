package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.AuthResult;
import com.fitness.core.auth.domain.User;

public interface IAuthUseCase {
    AuthResult register(RegisterCommand command);
    String login(LoginCommand command);
}