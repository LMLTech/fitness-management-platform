package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Subscription;
import com.fitness.core.auth.domain.SubscriptionFreezeLog;
import com.fitness.core.auth.port.in.ISubscriptionFreezeUseCase;
import com.fitness.core.auth.port.out.ISubscriptionFreezeRepositoryPort;
import com.fitness.core.auth.port.out.ISubscriptionRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionFreezeService implements ISubscriptionFreezeUseCase {

    private final ISubscriptionFreezeRepositoryPort freezeRepoPort;
    private final ISubscriptionRepositoryPort subscriptionRepoPort;

    @Override
    @Transactional
    public SubscriptionFreezeLog createFreezeRequest(UUID subscriptionId, String reason, String startStr, String endStr) {
        LocalDate start = LocalDate.parse(startStr);
        LocalDate end = LocalDate.parse(endStr);

        // 1. Kiểm tra tính hợp lệ của mốc thời gian nhập vào
        if (start.isBefore(LocalDate.now())) {
            throw new DomainException("INVALID_START_DATE", "Ngày bắt đầu bảo lưu không được nằm trong quá khứ");
        }
        if (!end.isAfter(start)) {
            throw new DomainException("INVALID_END_DATE", "Ngày kết thúc bảo lưu phải sau ngày bắt đầu ít nhất 1 ngày");
        }

        // 2. Kiểm tra xem hợp đồng gói tập có tồn tại và đang hoạt động không
        Subscription sub = subscriptionRepoPort.findById(subscriptionId)
                .orElseThrow(() -> new DomainException("SUBSCRIPTION_NOT_FOUND", "Hợp đồng gói tập không tồn tại"));

        if (!"Active".equalsIgnoreCase(sub.getStatus())) {
            throw new DomainException("INVALID_SUB_STATUS", "Chỉ cho phép bảo lưu những gói tập đang trạng thái Active");
        }

        if (end.isAfter(sub.getEndDate())) {
            throw new DomainException("EXCEEDS_EXPIRATION", "Thời hạn xin nghỉ vượt quá thời gian hiệu lực còn lại của gói tập");
        }

        // 3. Khởi tạo đơn lưu tạm ở trạng thái Pending
        SubscriptionFreezeLog log = SubscriptionFreezeLog.builder()
                .subscriptionId(subscriptionId)
                .freezeStart(start)
                .freezeEnd(end)
                .reason(reason)
                .status("Pending")
                .build();

        return freezeRepoPort.save(log);
    }

    @Override
    @Transactional
    public void processApproval(UUID logId, UUID adminUserId, boolean isApproved) {
        SubscriptionFreezeLog log = freezeRepoPort.findById(logId)
                .orElseThrow(() -> new DomainException("FREEZE_LOG_NOT_FOUND", "Không tìm thấy đơn yêu cầu bảo lưu tương ứng"));

        if (!"Pending".equalsIgnoreCase(log.getStatus())) {
            throw new DomainException("ALREADY_PROCESSED", "Đơn yêu cầu bảo lưu này đã được xử lý trước đó rồi");
        }

        if (!isApproved) {
            log.setStatus("Rejected");
            log.setApprovedBy(adminUserId);
            freezeRepoPort.save(log);
            return;
        }

        // NẾU CHẤP THUẬN BẢO LƯU: Tiến hành thuật toán tính bù ngày tập
        Subscription sub = subscriptionRepoPort.findById(log.getSubscriptionId())
                .orElseThrow(() -> new DomainException("SUBSCRIPTION_NOT_FOUND", "Hợp đồng gói tập đính kèm đơn không tồn tại"));

        // Tính khoảng số ngày nghỉ thực tế ví dụ nghỉ từ 20/05 đến 25/05 là nghỉ đúng 5 ngày
        long frozenDays = ChronoUnit.DAYS.between(log.getFreezeStart(), log.getFreezeEnd());

        // Thực hiện cộng bù số ngày nghỉ này vào mốc hết hạn gốc của gói tập
        sub.setEndDate(sub.getEndDate().plusDays(frozenDays));

        // Đổi trạng thái hợp đồng sang 'Frozen' để chặn không cho quét thẻ check-in đi tập
        sub.setStatus("Frozen");
        subscriptionRepoPort.save(sub);

        // Chốt đơn log bảo lưu thành công
        log.setStatus("Approved");
        log.setApprovedBy(adminUserId);
        freezeRepoPort.save(log);
    }
}