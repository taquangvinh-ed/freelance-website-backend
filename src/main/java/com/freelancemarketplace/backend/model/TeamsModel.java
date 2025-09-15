package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeamsModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    private String team_name;
    private String team_description;
    private String team_leader;
    private Integer hourly_rate;
    private String hour_per_week;
    private String team_avatar;
    private String team_banner;
    private Integer connections;
    private double wallet;
    private Boolean is_verified;

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

    @ManyToMany
    @JoinTable(
        name = "team_skills",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillsModel> skills;

}
