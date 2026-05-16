package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.port.out.IPaymentProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
public class VietQRAdapter implements IPaymentProvider {

    @Value("${app.payment.bank.bin}")
    private String bankBin;

    @Value("${app.payment.bank.account-no}")
    private String accountNo;

    @Value("${app.payment.bank.account-name}")
    private String accountName;

    @Override
    public String getPaymentInstructions(UUID paymentId, BigDecimal amount) {
        // Tách lấy 8 ký tự đầu của mã Payment để làm mã nội dung chuyển khoản tự động
        String checkoutCode = "FMP" + paymentId.toString().substring(0, 8).toUpperCase();

        // Chuẩn hóa tên chủ tài khoản thành định dạng URL thay khoảng trắng bằng %20 để ảnh hiển thị chuẩn xác
        String encodedName = accountName.replace(" ", "%20");

        // Sử dụng API VietQR mẫu compact sạch sẽ tự động điền số tài khoản, số tiền và nội dung chuyển khoản
        return String.format("https://img.vietqr.io/image/%s-%s-compact.png?amount=%s&addInfo=%s&accountName=%s",
                bankBin,
                accountNo,
                amount.toBigInteger().toString(),
                checkoutCode,
                encodedName);
    }

    @Override
    public boolean supports(String method) {
        // Hỗ trợ kiểm tra linh hoạt cả chuỗi
        return "BANK_TRANSFER".equalsIgnoreCase(method)
                || "Ngân hàng".equalsIgnoreCase(method)
                || "CreditCard".equalsIgnoreCase(method);
    }
}