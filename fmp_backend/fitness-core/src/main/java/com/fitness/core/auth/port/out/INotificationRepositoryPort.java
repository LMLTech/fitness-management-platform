package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Notification;
import java.util.List;
import java.util.UUID;

public interface INotificationRepositoryPort {
    void save(Notification notification);
    List<Notification> getNotificationsByUserId(UUID userId);
    void markAsRead(UUID notificationId);
}