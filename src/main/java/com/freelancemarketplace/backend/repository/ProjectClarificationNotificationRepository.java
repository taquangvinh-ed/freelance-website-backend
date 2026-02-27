package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ClarificationProjectQANotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectClarificationNotificationRepository extends JpaRepository<ClarificationProjectQANotificationModel, Long> {
    List<ClarificationProjectQANotificationModel> findTop10ByRecipientUserIdOrderByCreatedAtDesc(Long recipientUserId);
}
