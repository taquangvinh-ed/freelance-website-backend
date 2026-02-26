package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ProjectClarificationNotificationDTO {
    private Long questionId;
    private Long projectId;
    private String projectTitle;
    private String questionText;
    private String answerText;
    private String senderUsername;
    private String senderFirstName;
    private String senderLastName;
    private String senderAvatar;
    private NotificationType type; // NEW_QUESTION or NEW_ANSWER
    private Timestamp timestamp;

    public enum NotificationType {
        NEW_QUESTION,
        NEW_ANSWER
    }


}
