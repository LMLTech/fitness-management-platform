package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CreateTicketDto {
    private UUID userId;
    private String subject;
    private String priority;
    private String initialMessage;
}