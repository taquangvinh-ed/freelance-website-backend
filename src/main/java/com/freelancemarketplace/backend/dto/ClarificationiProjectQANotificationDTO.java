package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.model.ClarificationiProjectQANotificationModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ClarificationiProjectQANotificationDTO {
    private Long projectClarificationId;
    private Long questionId;
    private Long projectId;
    private Long recipientUserId; // User who receives the notification
    private String projectTitle;
    private String questionText;
    private String answerText;
    private String senderFirstName;
    private String senderLastName;
    private String senderAvatar;
    private String type;
    private boolean isRead;
    private Timestamp createdAt;
    private Timestamp readAt;
}
