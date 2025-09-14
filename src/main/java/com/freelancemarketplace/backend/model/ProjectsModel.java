package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ProjectStatus;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
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

    public ProjectsModel() {
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Double getBudget_amount() {
        return budget_amount;
    }

    public void setBudget_amount(Double budget_amount) {
        this.budget_amount = budget_amount;
    }

    public Integer getDuration_days() {
        return duration_days;
    }

    public void setDuration_days(Integer duration_days) {
        this.duration_days = duration_days;
    }

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }

    public Boolean getIs_Internship() {
        return is_Internship;
    }

    public void setIs_Internship(Boolean is_Internship) {
        this.is_Internship = is_Internship;
    }

    public List<VideosModel> getVideos() {
        return videos;
    }

    public void setVideos(List<VideosModel> videos) {
        this.videos = videos;
    }

    public PortfoliosModel getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfoliosModel portfolio) {
        this.portfolio = portfolio;
    }

    public CategoriesModel getCategory() {
        return category;
    }

    public void setCategory(CategoriesModel category) {
        this.category = category;
    }

    public BudgetsModel getBudget() {
        return budget;
    }

    public void setBudget(BudgetsModel budget) {
        this.budget = budget;
    }

    public FreelancersModel getProjectFreelancer() {
        return projectFreelancer;
    }

    public void setProjectFreelancer(FreelancersModel projectFreelancer) {
        this.projectFreelancer = projectFreelancer;
    }

    public Set<TestimonialsModel> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(Set<TestimonialsModel> testimonials) {
        this.testimonials = testimonials;
    }

    public Set<ProposalsModal> getProposalsList() {
        return proposalsList;
    }

    public void setProposalsList(Set<ProposalsModal> proposalsList) {
        this.proposalsList = proposalsList;
    }

    public ContractsModel getProjectContract() {
        return projectContract;
    }

    public void setProjectContract(ContractsModel projectContract) {
        this.projectContract = projectContract;
    }

    public Set<MessagesModel> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessagesModel> messages) {
        this.messages = messages;
    }

    public CompaniesModel getProjectCompany() {
        return projectCompany;
    }

    public void setProjectCompany(CompaniesModel projectCompany) {
        this.projectCompany = projectCompany;
    }

    public ClientsModel getProjectClient() {
        return projectClient;
    }

    public void setProjectClient(ClientsModel projectClient) {
        this.projectClient = projectClient;
    }

    public Set<SkillsModel> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillsModel> skills) {
        this.skills = skills;
    }
}
