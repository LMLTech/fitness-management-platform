package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.SupportTicket;
import com.fitness.core.auth.domain.TicketMessage;
import com.fitness.core.auth.port.out.ISupportTicketRepositoryPort;
import com.fitness.infrastructure.auth.entity.SupportTicketEntity;
import com.fitness.infrastructure.auth.entity.TicketMessageEntity;
import com.fitness.infrastructure.auth.repository.SupportTicketJpaRepository;
import com.fitness.infrastructure.auth.repository.TicketMessageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SupportTicketMysqlAdapter implements ISupportTicketRepositoryPort {

    private final SupportTicketJpaRepository ticketRepo;
    private final TicketMessageJpaRepository messageRepo;

    @Override
    public SupportTicket saveTicket(SupportTicket ticket) {
        SupportTicketEntity entity = SupportTicketEntity.builder()
                .id(ticket.getId())
                .userId(ticket.getUserId())
                .subject(ticket.getSubject())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .build();
        ticketRepo.save(entity);
        return ticket;
    }

    @Override
    public TicketMessage saveMessage(TicketMessage message) {
        TicketMessageEntity entity = TicketMessageEntity.builder()
                .id(message.getId())
                .ticketId(message.getTicketId())
                .senderId(message.getSenderId())
                .message(message.getMessage())
                .build();
        messageRepo.save(entity);
        return message;
    }

    @Override
    public Optional<SupportTicket> findTicketById(UUID ticketId) {
        return ticketRepo.findById(ticketId).map(entity -> SupportTicket.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .subject(entity.getSubject())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .build());
    }

    @Override
    public List<TicketMessage> findMessagesByTicketId(UUID ticketId) {
        return messageRepo.findByTicketId(ticketId).stream()
                .map(entity -> TicketMessage.builder()
                        .id(entity.getId())
                        .ticketId(entity.getTicketId())
                        .senderId(entity.getSenderId())
                        .message(entity.getMessage())
                        .build())
                .collect(Collectors.toList());
    }
}