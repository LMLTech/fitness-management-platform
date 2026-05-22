package com.fitness.core.auth.service;

import com.fitness.core.common.exception.DomainException;
import com.fitness.core.auth.domain.SupportTicket;
import com.fitness.core.auth.domain.TicketMessage;
import com.fitness.core.auth.domain.AuditTrace;
import com.fitness.core.auth.port.in.ISupportTicketUseCase;
import com.fitness.core.auth.port.out.ISupportTicketRepositoryPort;
import com.fitness.core.auth.port.in.INotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupportTicketService implements ISupportTicketUseCase {

    private final ISupportTicketRepositoryPort ticketRepoPort;
    private final INotificationUseCase notificationUseCase;

    @Override
    @Transactional
    public SupportTicket createTicket(UUID userId, String subject, String priority, String initialMessage) {
        SupportTicket ticket = SupportTicket.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .subject(subject)
                .status("Open")
                .priority(priority != null ? priority : "Normal")
                .build();
        ticketRepoPort.saveTicket(ticket);

        TicketMessage message = TicketMessage.builder()
                .id(UUID.randomUUID())
                .ticketId(ticket.getId())
                .senderId(userId)
                .message(initialMessage)
                .build();
        ticketRepoPort.saveMessage(message);

        return ticket;
    }

    @Override
    @Transactional
    public TicketMessage replyToTicket(UUID ticketId, UUID senderId, String messageText) {
        SupportTicket ticket = ticketRepoPort.findTicketById(ticketId)
                .orElseThrow(() -> new DomainException("TICKET_NOT_FOUND", "Không tìm thấy phiên hỗ trợ ticket"));

        if ("Resolved".equals(ticket.getStatus())) {
            throw new DomainException("TICKET_CLOSED", "Ticket hỗ trợ này đã được giải quyết và đóng lại");
        }

        TicketMessage message = TicketMessage.builder()
                .id(UUID.randomUUID())
                .ticketId(ticketId)
                .senderId(senderId)
                .message(messageText)
                .build();

        TicketMessage savedMessage = ticketRepoPort.saveMessage(message);

        // Bắn chuông nếu người reply KHÔNG PHẢI là chủ ticket (nghĩa là Admin trả lời)
        if (!senderId.equals(ticket.getUserId())) {
            notificationUseCase.createNotification(
                    ticket.getUserId(),
                    "Phản hồi Ticket mới 💬",
                    "Bộ phận hỗ trợ vừa trả lời khiếu nại của bạn: \"" + ticket.getSubject() + "\".",
                    "TICKET_REPLY"
            );
        }

        return savedMessage;
    }

    @Override
    @Transactional
    @AuditTrace(action = "RESOLVE", entityType = "SupportTicket")
    public void resolveTicket(UUID ticketId) {
        SupportTicket ticket = ticketRepoPort.findTicketById(ticketId)
                .orElseThrow(() -> new DomainException("TICKET_NOT_FOUND", "Không tìm thấy phiên hỗ trợ ticket"));

        ticket.setStatus("Resolved");
        ticketRepoPort.saveTicket(ticket);

        // Báo chuông khi đóng ticket
        notificationUseCase.createNotification(
                ticket.getUserId(),
                "Ticket đã được đóng 🔒",
                "Khiếu nại của bạn: \"" + ticket.getSubject() + "\" đã được giải quyết.",
                "TICKET_RESOLVED"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SupportTicket getTicketDetails(UUID ticketId) {
        SupportTicket ticket = ticketRepoPort.findTicketById(ticketId)
                .orElseThrow(() -> new DomainException("TICKET_NOT_FOUND", "Không tìm thấy phiên hỗ trợ ticket"));

        ticket.setMessages(ticketRepoPort.findMessagesByTicketId(ticketId));
        return ticket;
    }
}