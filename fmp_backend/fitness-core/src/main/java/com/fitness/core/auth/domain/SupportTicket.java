package com.fitness.core.auth.domain;

import lombok.*;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicket {
    private UUID id;
    private UUID userId;
    private String subject;
    private String status;    // 'Open', 'Resolved'
    private String priority;  // 'Low', 'Normal', 'High'
    private List<TicketMessage> messages;
}