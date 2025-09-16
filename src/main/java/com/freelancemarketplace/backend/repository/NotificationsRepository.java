package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends JpaRepository<NotificationModel, Long> {
  }