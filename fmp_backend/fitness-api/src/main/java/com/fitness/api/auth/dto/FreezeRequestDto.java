package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class FreezeRequestDto {
    private UUID subscriptionId;
    private String freezeStart; // Định dạng chuẩn: YYYY-MM-DD
    private String freezeEnd;   // Định dạng chuẩn: YYYY-MM-DD
    private String reason;
}