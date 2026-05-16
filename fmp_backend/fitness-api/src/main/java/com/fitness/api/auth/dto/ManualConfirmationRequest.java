package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ManualConfirmationRequest {
    private UUID paymentId;
    private String referenceNote; // Ghi chú lý do hoặc mã biên lai thu tiền mặt
}