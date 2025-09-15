package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TestimonialsModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testimonial_id;

    private String rating;
    private String comment;

    // The date that the testimonial was given
    private String date;


    private Boolean testiminial_back;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerTestimonials;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectsModel projectTestimonials;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientsModel clientTestimonials;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompaniesModel companyTestimonials;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamsModel teamTestimonials;

}
