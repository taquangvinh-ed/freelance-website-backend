package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ReviewTypes;
import com.freelancemarketplace.backend.enums.ReviewerRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Testimonials")
public class TestimonialModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testimonialId;

    private Integer ratingScore;

    @Column(length = 1000)
    private String comment;

    private Timestamp datePosted;

    private Boolean isResponded;

    @Enumerated(EnumType.STRING)
    private ReviewerRoles reviewerRole;

    @Enumerated(EnumType.STRING)
    private ReviewTypes type;

    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private ProjectModel project;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private ClientModel client;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private TeamModel team;

}
