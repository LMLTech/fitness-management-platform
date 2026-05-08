package com.fitness.core.auth.port.in;

import java.util.Map;
import java.util.UUID;

public interface ITwoFactorUseCase {
    Map<String, String> setup2FA(UUID userId);
    boolean enable2FA(UUID userId, int otpCode);
}
