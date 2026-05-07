package com.fitness.core.auth.port.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginCommand {
    private String email;
    private String rawPassword;
}