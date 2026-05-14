package com.fitness.api.auth.dto;

import com.fitness.core.auth.domain.LeaveStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewLeaveRequestDto {
    // Trạng thái duyệt đơn nghỉ phép: APPROVED (duyệt) hoặc REJECTED (từ chối)
    private LeaveStatus status;
}