package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPoint {
    private UUID id;
    private UUID memberId;
    private Integer pointsChange;
    private String reason;
    private LocalDateTime createdAt;
}