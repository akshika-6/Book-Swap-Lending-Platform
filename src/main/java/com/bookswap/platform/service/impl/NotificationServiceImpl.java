package com.bookswap.platform.service.impl;

import com.bookswap.platform.dto.NotificationRequestDto;
import com.bookswap.platform.exception.ResourceNotFoundException;
import com.bookswap.platform.model.AppUser;
import com.bookswap.platform.model.Notification;
import com.bookswap.platform.repository.NotificationRepository;
import com.bookswap.platform.repository.UserRepository;
import com.bookswap.platform.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Notification createNotification(NotificationRequestDto request) {
        AppUser user = loadUser(request.userId());

        Notification notification = Notification.builder()
                .user(user)
                .message(request.message())
                .unread(true)
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        AppUser user = loadUser(userId);
        return notificationRepository.findByUser(user);
    }

    @Override
    @Transactional
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        notification.setUnread(false);
        return notification;
    }

    private AppUser loadUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}