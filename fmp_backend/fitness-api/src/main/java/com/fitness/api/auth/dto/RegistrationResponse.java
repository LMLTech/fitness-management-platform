package com.fitness.api.auth.dto;

import com.fitness.core.auth.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationResponse {
    private User user;
    private String memberCode;
    private String referralCode;
}