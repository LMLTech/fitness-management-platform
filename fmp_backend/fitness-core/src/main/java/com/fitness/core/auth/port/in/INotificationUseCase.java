package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Notification;
import java.util.List;
import java.util.UUID;

public interface INotificationUseCase {
    void createNotification(UUID userId, String title, String message, String type);
    List<Notification> getUserNotifications(UUID userId);
    void markAsRead(UUID notificationId);
}
