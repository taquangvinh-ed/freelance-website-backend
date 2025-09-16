package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
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


    @OneToMany(mappedBy = "project")
    private List<VideoModel> videos;

    @OneToOne(mappedBy = "project")
    private PortfolioModel portfolio;

    @ManyToOne
    @JoinColumn(name="categoryId", nullable=false)
    private CategoryModel category;

    @ManyToOne
    @JoinColumn(name= "budgetId")
    private BudgetModel budget;

    @ManyToOne
    @JoinColumn(name ="freelancerId")
    private FreelancerModel Freelancer;

    @OneToMany(mappedBy = "project")
    private Set<TestimonialModel> testimonials;

    @OneToMany(mappedBy = "project")
    private Set<ProposalModal> proposals;

    @OneToOne(mappedBy = "contractProject")
    private ContractModel contract;

    @OneToMany(mappedBy = "project")
    private Set<MessageModel> messages;

    //The company that posts the project
    @ManyToOne
    @JoinColumn(name = "companyId")
    private CompanyModel company;

    //The client that posts the project
    @ManyToOne
    @JoinColumn(name = "clientId")
    private ClientModel client;

    @ManyToMany
    @JoinTable(
            name = "project_skills",
            joinColumns = @JoinColumn(name = "projectId"),
            inverseJoinColumns = @JoinColumn(name = "skillId")
    )
    private Set<SkillModel> skills;

}
