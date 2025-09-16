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
@Table(name = "Teams")
public class TeamModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    private String name;
    private String description;
    private String leader;
    private Integer hourlyRate;
    private String hourPerWeek;
    private String avatar;
    private String banner;
    private Integer connections;
    private double wallet;
    private Boolean isVerified;

    @OneToMany(mappedBy = "team")
    private List<VideoModel> videos;

    @ManyToMany
    @JoinTable(
        name = "team_freelancers",
        joinColumns = @JoinColumn(name = "teamId"),
        inverseJoinColumns = @JoinColumn(name = "freelancerId")
    )
    private List<FreelancerModel> members;


    @OneToMany(mappedBy = "team")
    private Set<TestimonialModel> testimonials;

    @OneToMany(mappedBy = "team")
    private Set<ProposalModal> proposals;

    @OneToMany(mappedBy = "team")
    private Set<PaymentModel> payments;

    @OneToMany(mappedBy = "team")
    private Set<ContractModel> contracts;

    @OneToMany(mappedBy = "team")
    private Set<MessageModel> messages;

    @ManyToMany
    @JoinTable(
        name = "team_skills",
        joinColumns = @JoinColumn(name = "teamId"),
        inverseJoinColumns = @JoinColumn(name = "skillId")
    )
    private Set<SkillModel> skills;

}
