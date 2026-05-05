package com.bookswap.platform.service;

import com.bookswap.platform.dto.NotificationRequestDto;
import com.bookswap.platform.model.Notification;
import java.util.List;

public interface NotificationService {
    Notification createNotification(NotificationRequestDto request);

    List<Notification> getUserNotifications(Long userId);

    Notification markAsRead(Long notificationId);
}