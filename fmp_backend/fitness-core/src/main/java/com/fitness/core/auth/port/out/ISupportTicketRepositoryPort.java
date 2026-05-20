package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.SupportTicket;
import com.fitness.core.auth.domain.TicketMessage;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ISupportTicketRepositoryPort {
    SupportTicket saveTicket(SupportTicket ticket);
    TicketMessage saveMessage(TicketMessage message);
    Optional<SupportTicket> findTicketById(UUID ticketId);
    List<TicketMessage> findMessagesByTicketId(UUID ticketId);
}