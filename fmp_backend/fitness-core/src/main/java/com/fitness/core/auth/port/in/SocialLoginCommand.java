package com.fitness.core.auth.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginCommand {
    private String idToken;
    private String provider; // "GOOGLE"
}