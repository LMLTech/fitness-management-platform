package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Booking {
    private UUID id;
    private UUID memberId; // id của người dùng
    private UUID sessionId; // id của buổi học
    private String status;  // 'Confirmed', 'Cancelled', 'Attended'
    private LocalDateTime checkedInAt;
}