package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ReplyTicketDto {
    private UUID senderId;
    private String message;
}