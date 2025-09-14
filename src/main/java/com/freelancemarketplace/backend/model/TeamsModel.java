package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class TeamsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    @OneToMany(mappedBy = "team")
    private List<VideosModel> videos;

    @ManyToMany
    @JoinTable(
        name = "team_freelancers",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "freelancer_id")
    )
    private List<FreelancersModel> teamFreelancers;


    @OneToMany(mappedBy = "teamTestimonials")
    private Set<TestimonialsModel> testimonials;

    @OneToMany(mappedBy = "teamProposals")
    private Set<ProposalsModal> proposalsList;

    @OneToMany(mappedBy = "teamPayments")
    private Set<PaymentsModel> paymentsTeam;

    @OneToMany(mappedBy = "contractTeam")
    private Set<ContractsModel> contractsTeam;

    @OneToMany(mappedBy = "teamMessages")
    private Set<MessagesModel> messages;

}
