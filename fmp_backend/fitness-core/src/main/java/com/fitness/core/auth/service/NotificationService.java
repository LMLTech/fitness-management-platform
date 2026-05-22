package com.fitness.core.auth.service; // Đã đổi về auth

import com.fitness.core.auth.domain.Notification;
import com.fitness.core.auth.port.in.INotificationUseCase;
import com.fitness.core.auth.port.out.INotificationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationUseCase {

    private final INotificationRepositoryPort repositoryPort;

    @Override
    public void createNotification(UUID userId, String title, String message, String type) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .build();
        repositoryPort.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(UUID userId) {
        return repositoryPort.getNotificationsByUserId(userId);
    }

    @Override
    public void markAsRead(UUID notificationId) {
        repositoryPort.markAsRead(notificationId);
    }
}