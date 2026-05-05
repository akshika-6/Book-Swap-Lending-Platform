package com.bookswap.platform.repository;

import com.bookswap.platform.model.AppUser;
import com.bookswap.platform.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(AppUser user);

    List<Notification> findByUnreadTrue();
}