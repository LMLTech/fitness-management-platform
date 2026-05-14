package com.fitness.core.auth.domain;

// Enum dùng để biểu diễn trạng thái của đơn xin nghỉ phép
public enum LeaveStatus {
    PENDING,   // Đang chờ duyệt
    APPROVED,  // Đã được phê duyệt
    REJECTED   // Bị từ chối
}