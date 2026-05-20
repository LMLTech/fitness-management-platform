package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "ticket_messages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketMessageEntity {
    @Id
    private UUID id;

    @Column(name = "ticket_id", nullable = false)
    private UUID ticketId;

    @Column(name = "sender_id", nullable = false)
    private UUID senderId;

    @Column(columnDefinition = "TEXT")
    private String message;
}