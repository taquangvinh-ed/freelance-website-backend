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
public class ProjectsModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private Double budget_amount;
    private Integer duration_days;
    private Integer connections;
    private Timestamp start_date;
    private Timestamp end_date;

    // If the project is an internship
    private Boolean is_Internship;


    @OneToMany(mappedBy = "project")
    private List<VideosModel> videos;

    @OneToOne(mappedBy = "project")
    private PortfoliosModel portfolio;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private CategoriesModel category;

    @ManyToOne
    @JoinColumn(name= "budget_id")
    private BudgetsModel budget;

    @ManyToOne
    @JoinColumn(name ="freelancer_id")
    private FreelancersModel projectFreelancer;

    @OneToMany(mappedBy = "projectTestimonials" )
    private Set<TestimonialsModel> testimonials;

    @OneToMany(mappedBy = "projectProposals")
    private Set<ProposalsModal> proposalsList;

    @OneToOne(mappedBy = "contractProject")
    private ContractsModel projectContract;

    @OneToMany(mappedBy = "projectMessages")
    private Set<MessagesModel> messages;

    //The company that posts the project
    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompaniesModel projectCompany;

    //The client that posts the project
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientsModel projectClient;

    @ManyToMany
    @JoinTable(
            name = "project_skills",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillsModel> skills;

}
