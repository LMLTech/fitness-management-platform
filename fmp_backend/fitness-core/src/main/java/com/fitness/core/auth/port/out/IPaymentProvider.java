package com.fitness.core.auth.port.out;

import java.math.BigDecimal;
import java.util.UUID;

public interface IPaymentProvider {
    // Hàm chịu trách nhiệm sinh thông tin chuỗi dữ liệu
    String getPaymentInstructions(UUID paymentId, BigDecimal amount);

    // Hàm phân định xem lớp Adapter có chịu trách nhiệm xử lý phương thức thanh toán này không
    boolean supports(String method);
}