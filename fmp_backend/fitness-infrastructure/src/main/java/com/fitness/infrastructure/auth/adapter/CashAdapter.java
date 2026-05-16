package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.port.out.IPaymentProvider;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
public class CashAdapter implements IPaymentProvider {

    @Override
    public String getPaymentInstructions(UUID paymentId, BigDecimal amount) {
        // Trả về chuỗi thông báo nghiệp vụ hướng dẫn cụ thể cho khách hàng đến quầy giao dịch
        return "Vui lòng di chuyển đến trực tiếp quầy Lễ tân của chi nhánh phòng tập gần nhất để hoàn tất thanh toán tiền mặt. Mã hóa đơn của bạn: "
                + paymentId.toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean supports(String method) {
        // Khớp với giá trị "CASH", "Cash" hoặc chữ tiếng Việt "Tiền mặt" trong database
        return "CASH".equalsIgnoreCase(method)
                || "Cash".equalsIgnoreCase(method)
                || "Tiền mặt".equalsIgnoreCase(method);
    }
}