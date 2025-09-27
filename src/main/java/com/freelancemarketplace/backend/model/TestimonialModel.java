package com.freelancemarketplace.backend.model;

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

    private String rating;
    private String comment;

    // The date that the testimonial was given
    private Timestamp date;


    private Boolean testiminialBack;

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
    @JoinColumn(name = "companyId")
    private CompanyModel company;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private TeamModel team;

}
