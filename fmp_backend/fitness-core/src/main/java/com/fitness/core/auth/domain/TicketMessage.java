package com.fitness.core.auth.domain;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketMessage {
    private UUID id;
    private UUID ticketId;
    private UUID senderId;
    private String message;
}