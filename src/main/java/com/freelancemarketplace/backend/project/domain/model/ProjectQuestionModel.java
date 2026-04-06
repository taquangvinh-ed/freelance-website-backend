package com.freelancemarketplace.backend.project.domain.model;

import com.freelancemarketplace.backend.admin.domain.enums.QuestionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.user.domain.model.UserModel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "project_questions")
public class ProjectQuestionModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    @Enumerated(EnumType.STRING)
    private QuestionStatus status = QuestionStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    private ProjectModel project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "askedByUserId", nullable = false)
    private UserModel askedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answeredByUserId")
    private UserModel answeredBy;

    private Timestamp createdAt;

    private Timestamp answeredAt;
}

