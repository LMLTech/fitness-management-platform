package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.AuthResult;
import com.fitness.core.auth.domain.User;
import java.util.Map;
import java.util.UUID;

public interface IAuthUseCase {
    AuthResult register(RegisterCommand command);
    String login(LoginCommand command);

    // thiết lâpj google Authenticator
    Map<String, String> setup2FA(UUID userId);

    // Xác nhận bật 2FA
    void enable2FA(UUID userId, int otpCode);
}