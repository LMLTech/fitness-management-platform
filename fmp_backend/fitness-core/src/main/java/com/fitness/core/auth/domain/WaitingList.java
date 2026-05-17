package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Builder
public class WaitingList {
    private UUID id;
    private UUID memberId;
    private UUID sessionId;
    private Integer position;
    private String status; // 'Waiting', 'Promoted', 'Cancelled'
}