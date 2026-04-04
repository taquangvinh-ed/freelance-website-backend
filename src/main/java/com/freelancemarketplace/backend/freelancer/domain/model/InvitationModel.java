package com.freelancemarketplace.backend.freelancer.domain.model;

import com.freelancemarketplace.backend.freelancer.domain.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "invitation")
public class InvitationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectModel project;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancerModel freelancer;

    @Column(name = "invited_at")
    private LocalDateTime invitedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InvitationStatus status; // PENDING, ACCEPTED, REJECTED


}
