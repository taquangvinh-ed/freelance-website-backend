package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class ProjectsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;


    @OneToMany(mappedBy = "project")
    private List<VideosModel> videos;

    @OneToOne(mappedBy = "project")
    private PortfoliosModel portfolio;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Categories category;

    @ManyToOne
    @JoinColumn(name= "budget_id")
    private Budgets budget;

    @ManyToOne
    @JoinColumn(name ="freelancer_id")
    private FreelancersModel projectFreelancer;

    @OneToMany(mappedBy = "projectTestimonials" )
    private Set<TestimonialsModel> testimonials;

    @OneToMany(mappedBy = "projectProposals")
    private Set<ProposalsModal> proposalsList;

}
