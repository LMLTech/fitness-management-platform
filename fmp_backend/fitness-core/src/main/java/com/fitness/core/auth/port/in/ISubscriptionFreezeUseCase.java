package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.SubscriptionFreezeLog;
import java.util.UUID;

public interface ISubscriptionFreezeUseCase {
    // Hội viên tự tạo đơn yêu cầu xin tạm dừng chuỗi ngày tập
    SubscriptionFreezeLog createFreezeRequest(UUID subscriptionId, String reason, String startStr, String endStr);

    // Lễ tân hoặc Admin xử lý phê duyệt Chấp thuận / Từ chối
    void processApproval(UUID logId, UUID adminUserId, boolean isApproved);
}