package com.fitness.core.auth.port.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterCommand {
    private String username;
    private String email;
    private String phoneNumber;
    private String rawPassword;
    private String fullName;
}