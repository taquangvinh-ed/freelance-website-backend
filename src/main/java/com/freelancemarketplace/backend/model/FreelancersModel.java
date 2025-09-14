package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Map;
import java.util.Set;

@Entity
public class FreelancersModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freelancer_id;

    private String first_name;
    private String last_name;
    private String title;


    private String profile_picture;
    private String email;
    private String phone_number;
    private String password_hash;
    private Boolean isVerified;
    private Double hourly_rate;

//    @Type(JsonBinaryType.class)
//    @Column(columnDefinition = "jsonb")
//    private Map<String, Object> bio;

    private Double wallet;
    private Integer connections;
    private Integer hours_per_week;
    private Boolean isBlocked;



    @OneToMany(mappedBy = "freelancer")
    private Set<VideosModel> videoList;

    @OneToOne(mappedBy = "freelancer")
    private PortfoliosModel portfolio;

    @ManyToMany
    @JoinTable(
            name = "freelancer_languages",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set <LanguagesModel> freelancer_languages;

    @OneToOne
    @JoinColumn(name = "analytics_id")
    private AnalyticsModel freelancer_analytics;

    @ManyToOne
    @JoinColumn(name="location_id")
    private LocationsModel freelancer_location;

    @OneToMany(mappedBy = "freelancerMessages")
    private  Set<MessagesModel> messagesList;

    @OneToMany(mappedBy = "contractFreelancer")
    private Set<ContractsModel> contractsList;

    @OneToMany(mappedBy = "projectFreelancer")
    private Set<ProjectsModel> projectsList;

    @ManyToMany
    @JoinTable(
            name = "freelancer_skills",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillsModel> skills;

    @OneToMany(mappedBy = "freelancerCertificates" )
    private Set<CertificatesModel> certificatesList;

    @OneToMany(mappedBy = "freelancerEducations" )
    private Set<EducationsModel> educationList;

    @OneToMany(mappedBy = "freelancerProposals" )
    private Set<ProposalsModal> proposalsList;

    @ManyToMany(mappedBy="freelancerTests" )
    private Set<TestsModel> testsList;

    @OneToMany(mappedBy = "freelancerTestimonials" )
    private Set<TestimonialsModel> testimonialsList;

    //Team that the freelancer is part of
    @ManyToMany(mappedBy = "teamFreelancers" )
    private Set<TeamsModel> teams;

    @OneToMany(mappedBy = "freelancerExperiences" )
    private Set<ExperiencesModel> experiencesList;

    @OneToMany(mappedBy = "freelancerPayments" )
    private Set<PaymentsModel> paymentsList;

    @OneToMany(mappedBy = "freelancerReports" )
    private Set<ReportsModel> reportsList;

    @OneToMany(mappedBy = "freelancerProduct")
    private Set<ProductsModel> productsList;

    public FreelancersModel() {
    }

    public Long getFreelancer_id() {
        return freelancer_id;
    }

    public void setFreelancer_id(Long freelancer_id) {
        this.freelancer_id = freelancer_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Double getHourly_rate() {
        return hourly_rate;
    }

    public void setHourly_rate(Double hourly_rate) {
        this.hourly_rate = hourly_rate;
    }

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public Integer getHours_per_week() {
        return hours_per_week;
    }

    public void setHours_per_week(Integer hours_per_week) {
        this.hours_per_week = hours_per_week;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public Set<VideosModel> getVideoList() {
        return videoList;
    }

    public void setVideoList(Set<VideosModel> videoList) {
        this.videoList = videoList;
    }

    public PortfoliosModel getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfoliosModel portfolio) {
        this.portfolio = portfolio;
    }

    public Set<LanguagesModel> getFreelancer_languages() {
        return freelancer_languages;
    }

    public void setFreelancer_languages(Set<LanguagesModel> freelancer_languages) {
        this.freelancer_languages = freelancer_languages;
    }

    public AnalyticsModel getFreelancer_analytics() {
        return freelancer_analytics;
    }

    public void setFreelancer_analytics(AnalyticsModel freelancer_analytics) {
        this.freelancer_analytics = freelancer_analytics;
    }

    public LocationsModel getFreelancer_location() {
        return freelancer_location;
    }

    public void setFreelancer_location(LocationsModel freelancer_location) {
        this.freelancer_location = freelancer_location;
    }

    public Set<MessagesModel> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(Set<MessagesModel> messagesList) {
        this.messagesList = messagesList;
    }

    public Set<ContractsModel> getContractsList() {
        return contractsList;
    }

    public void setContractsList(Set<ContractsModel> contractsList) {
        this.contractsList = contractsList;
    }

    public Set<ProjectsModel> getProjectsList() {
        return projectsList;
    }

    public void setProjectsList(Set<ProjectsModel> projectsList) {
        this.projectsList = projectsList;
    }

    public Set<SkillsModel> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillsModel> skills) {
        this.skills = skills;
    }

    public Set<CertificatesModel> getCertificatesList() {
        return certificatesList;
    }

    public void setCertificatesList(Set<CertificatesModel> certificatesList) {
        this.certificatesList = certificatesList;
    }

    public Set<EducationsModel> getEducationList() {
        return educationList;
    }

    public void setEducationList(Set<EducationsModel> educationList) {
        this.educationList = educationList;
    }

    public Set<ProposalsModal> getProposalsList() {
        return proposalsList;
    }

    public void setProposalsList(Set<ProposalsModal> proposalsList) {
        this.proposalsList = proposalsList;
    }

    public Set<TestsModel> getTestsList() {
        return testsList;
    }

    public void setTestsList(Set<TestsModel> testsList) {
        this.testsList = testsList;
    }

    public Set<TestimonialsModel> getTestimonialsList() {
        return testimonialsList;
    }

    public void setTestimonialsList(Set<TestimonialsModel> testimonialsList) {
        this.testimonialsList = testimonialsList;
    }

    public Set<TeamsModel> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamsModel> teams) {
        this.teams = teams;
    }

    public Set<ExperiencesModel> getExperiencesList() {
        return experiencesList;
    }

    public void setExperiencesList(Set<ExperiencesModel> experiencesList) {
        this.experiencesList = experiencesList;
    }

    public Set<PaymentsModel> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(Set<PaymentsModel> paymentsList) {
        this.paymentsList = paymentsList;
    }

    public Set<ReportsModel> getReportsList() {
        return reportsList;
    }

    public void setReportsList(Set<ReportsModel> reportsList) {
        this.reportsList = reportsList;
    }

    public Set<ProductsModel> getProductsList() {
        return productsList;
    }

    public void setProductsList(Set<ProductsModel> productsList) {
        this.productsList = productsList;
    }
}
