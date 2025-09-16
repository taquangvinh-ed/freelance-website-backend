package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {

    NotificationDTO createNotification(NotificationDTO notificationDTO);

    NotificationDTO updateNotification(Long notificationId, NotificationDTO notificationDTO);

    Boolean deleteNotification(Long notificationId);

    List<NotificationDTO>getAllNotification();
}
