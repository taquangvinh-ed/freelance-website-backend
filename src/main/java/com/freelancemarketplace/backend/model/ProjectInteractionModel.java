package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.InteractionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "project_interaction")
public class ProjectInteractionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectInteractionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancerId", nullable = false)
    private FreelancerModel freelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    private ProjectModel project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionType interactionType;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    private boolean isPositive;
}
