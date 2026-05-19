package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {
    private UUID id;
    private String name;
    private String rules;
    private Integer rewardPoints;
    private UUID targetBadgeId;
    private LocalDateTime deletedAt;
}