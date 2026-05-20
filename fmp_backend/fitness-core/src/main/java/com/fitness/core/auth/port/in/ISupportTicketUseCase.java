package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.SupportTicket;
import com.fitness.core.auth.domain.TicketMessage;
import java.util.UUID;

public interface ISupportTicketUseCase {
    SupportTicket createTicket(UUID userId, String subject, String priority, String initialMessage);
    TicketMessage replyToTicket(UUID ticketId, UUID senderId, String message);
    void resolveTicket(UUID ticketId);
    SupportTicket getTicketDetails(UUID ticketId);
}