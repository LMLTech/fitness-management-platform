package com.fitness.core.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private UUID id;
    private UUID userId;
    private String addressType; // 'Home', 'Work', etc.
    private String streetAddress;
    private String ward;
    private String district;
    private String city;
    private String country;
}