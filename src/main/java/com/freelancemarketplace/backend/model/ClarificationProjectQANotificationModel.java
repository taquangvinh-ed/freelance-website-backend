package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "clarification_project_qa_notifications")
@Getter
@Setter
@NoArgsConstructor
public class ClarificationProjectQANotificationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private NotificationType type;
    private boolean isRead; // Track if notification is read
    private Timestamp createdAt;
    private Timestamp readAt;

    public enum NotificationType {
        NEW_QUESTION,
        NEW_ANSWER
    }

}
