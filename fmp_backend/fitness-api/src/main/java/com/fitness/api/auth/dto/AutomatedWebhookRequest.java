package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutomatedWebhookRequest {
    private String description;        // Nội dung tin nhắn chuyển khoản nhận được có chứa FMPXXXXX
    private String gatewayReference;   // Mã giao dịch của phía ngân hàng/MoMo mã FT hoặc số tham chiếu
}