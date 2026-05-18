package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Payment;
import java.util.Optional;
import java.util.UUID;

public interface IPaymentRepositoryPort {
    Payment save(Payment payment);  // Lưu payment mới hoặc cập nhật payment
    Optional<Payment> findById(UUID id); // Tìm payment theo UUID đầy đủ
    // Tìm payment trạng thái PENDING bằng phần đầu UUID dùng khi user nhập mã ngắn
    Optional<Payment> findPendingPaymentByUuidPrefix(String prefix);
    // Để OrderService có quyền gọi lệnh lưu hóa đơn
    Payment savePayment(Payment payment);
}