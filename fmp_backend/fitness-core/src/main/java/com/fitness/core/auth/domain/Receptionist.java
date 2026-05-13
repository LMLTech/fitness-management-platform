package com.fitness.core.auth.domain;

import lombok.*;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receptionist {
    private UUID userId;
    private String shift;
}
