package com.fitness.core.auth.port.in;

import java.util.UUID;

public interface IPaymentConfirmationUseCase {
    // Luồng tự động nhận diện từ dịch vụ quét biến động số dư ngân hàng/MoMo
    void confirmPaymentAutomatically(String transferDescription, String gatewayTransactionCode, String secretWebhookToken);

    // Luồng xử lý thủ công bằng tay của Lễ tân/Quản trị viên
    void confirmPaymentManually(UUID paymentId, String referenceCode);
}