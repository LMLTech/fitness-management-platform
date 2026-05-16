package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.port.out.IPaymentProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

@Component
public class MoMoAdapter implements IPaymentProvider {

    @Value("${app.payment.momo.account-name}")
    private String momoPhone;

    @Override
    public String getPaymentInstructions(UUID paymentId, BigDecimal amount) {
        // 1. Tạo mã nội dung chuyển khoản tự động
        String checkoutCode = "FMP" + paymentId.toString().substring(0, 8).toUpperCase();

        // 2. Định dạng số tiền thành dạng VND cho đẹp và dễ nhìn
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedAmount = currencyFormat.format(amount);

        // 3. In ra chuỗi chỉ dẫn đầy đủ bao gồm cả Link sạch và Số tiền
        return String.format("LINK_MOMO: https://me.momo.vn/%s | SO_TIEN: %s | NOI_DUNG: %s",
                momoPhone,
                formattedAmount,
                checkoutCode);
    }

    @Override
    public boolean supports(String method) {
        return "MOMO".equalsIgnoreCase(method) || "MoMo".equalsIgnoreCase(method);
    }
}