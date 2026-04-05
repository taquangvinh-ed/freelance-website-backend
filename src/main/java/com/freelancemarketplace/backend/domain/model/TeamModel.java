package com.freelancemarketplace.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.conversation.domain.model.ConversationModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.domain.model.VideoModel;
import com.freelancemarketplace.backend.payment.domain.model.PaymentModel;
import com.freelancemarketplace.backend.proposal.domain.model.ProposalModel;
import com.freelancemarketplace.backend.review.domain.model.TestimonialModel;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;

@Entity(name = "LegacyTeamModel")
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "team_freelancers",
        joinColumns = @JoinColumn(name = "teamId"),
        inverseJoinColumns = @JoinColumn(name = "freelancerId")
    )
    private List<FreelancerModel> members;


    @OneToMany(mappedBy = "team")
    private Set<TestimonialModel> testimonials;

    @OneToMany(mappedBy = "team")
    private Set<ProposalModel> proposals;

    @OneToMany(mappedBy = "team")
    private Set<PaymentModel> payments;

    @OneToMany(mappedBy = "team")
    private Set<ContractModel> contracts;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "team_skills",
        joinColumns = @JoinColumn(name = "teamId"),
        inverseJoinColumns = @JoinColumn(name = "skillId")
    )
    private Set<SkillModel> skills;

    @OneToMany(mappedBy = "team")
    private Set<ConversationModel> conversation = new HashSet<>();

}
