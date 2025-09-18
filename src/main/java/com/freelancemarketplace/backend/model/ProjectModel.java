package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Projects")
public class ProjectModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private Double budgetAmount;
    private Integer durationDays;
    private Integer connections;
    private Timestamp startDate;
    private Timestamp endDate;

    // If the project is an internship
    private Boolean isInternship;


    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<VideoModel> videos;

    @OneToOne(mappedBy = "project" )
    private PortfolioModel portfolio;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name="categoryId")
    private CategoryModel category;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name= "budgetId")
    private BudgetModel budget;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name ="freelancerId")
    private FreelancerModel Freelancer;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<TestimonialModel> testimonials;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<ProposalModal> proposals;

    @OneToOne(mappedBy = "contractProject", fetch = FetchType.LAZY)
    private ContractModel contract;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<MessageModel> messages;

    //The company that posts the project
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId")
    private CompanyModel company;

    //The client that posts the project
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private ClientModel client;

    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_skills",
            joinColumns = @JoinColumn(name = "projectId"),
            inverseJoinColumns = @JoinColumn(name = "skillId")
    )
    private Set<SkillModel> skills = new HashSet<>();

}
