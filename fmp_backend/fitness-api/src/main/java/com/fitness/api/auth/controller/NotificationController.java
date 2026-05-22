package com.fitness.api.auth.controller;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Notification;
import com.fitness.core.auth.port.in.INotificationUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final INotificationUseCase notificationUseCase;

    // =================================================================
    // DTO: Nhận dữ liệu gửi thông báo từ Frontend/Postman
    // =================================================================
    @Data
    public static class SendNotificationReq {
        private UUID userId;
        private String title;
        private String message;
        private String type;
    }
    // Gửi thông báo thủ công tới 1 user
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> sendManualNotification(@RequestBody SendNotificationReq req) {
        notificationUseCase.createNotification(req.getUserId(), req.getTitle(), req.getMessage(), req.getType());
        return ResponseEntity.ok(ApiResponse.success(null, "Đã gửi thông báo thành công tới hội viên!"));
    }

    // Lấy danh sách thông báo quả chuông
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Notification>>> getUserNotifications(@PathVariable UUID userId) {
        List<Notification> notifications = notificationUseCase.getUserNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Lấy danh sách thông báo thành công!"));
    }
    // API 3 Nhấn vào thông báo để đánh dấu đã đọc
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable UUID notificationId) {
        notificationUseCase.markAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã đánh dấu đọc thông báo!"));
    }
}