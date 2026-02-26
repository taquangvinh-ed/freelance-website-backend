package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ProjectQuestionDTO {
    private Long questionId;
    private Long projectId;
    private String questionText;
    private String answerText;
    private String status;
    private Long askedByUserId;
    private String askedByFirstName;
    private String askedByLastName;
    private String askedByAvatar;
    private String askedByUsername;
    private Long answeredByUserId;
    private String answeredByUsername;
    private String answeredByFirstName;
    private String answeredByLastName;
    private String answeredByAvatar;
    private Timestamp createdAt;
    private Timestamp answeredAt;
}

