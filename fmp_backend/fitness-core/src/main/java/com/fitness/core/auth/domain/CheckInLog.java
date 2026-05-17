package com.fitness.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CheckInLog {
    private UUID id;
    private UUID userId;       // Có thể NULL nếu là khách vãng lai
    private UUID sessionId;    // Có thể NULL nếu khách chỉ đến tham quan/tập tự do
    private String accessMethod; // 'Card', 'QR', 'Manual', 'Guest'
    private LocalDateTime checkInTime;
    private UUID processedBy;  // ID của nhân viên lễ tân thực hiện (nếu có)
}