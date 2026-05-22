package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Notification;
import com.fitness.core.auth.port.out.INotificationRepositoryPort;
import com.fitness.infrastructure.auth.entity.NotificationEntity;
import com.fitness.infrastructure.auth.repository.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationMysqlAdapter implements INotificationRepositoryPort {

    private final NotificationJpaRepository repo;

    @Override
    public void save(Notification notification) {
        NotificationEntity entity = NotificationEntity.builder()
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .build();
        repo.save(entity);
    }

    @Override
    public List<Notification> getNotificationsByUserId(UUID userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(entity -> Notification.builder()
                        .id(entity.getId())
                        .userId(entity.getUserId())
                        .title(entity.getTitle())
                        .message(entity.getMessage())
                        .type(entity.getType())
                        .isRead(entity.getIsRead())
                        .createdAt(entity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(UUID notificationId) {
        repo.findById(notificationId).ifPresent(entity -> {
            entity.setIsRead(true);
            repo.save(entity);
        });
    }
}