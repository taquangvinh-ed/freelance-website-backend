package com.freelancemarketplace.backend.notification.infrastructure.repository;

import com.freelancemarketplace.backend.notification.domain.model.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends JpaRepository<NotificationModel, Long> {
  }