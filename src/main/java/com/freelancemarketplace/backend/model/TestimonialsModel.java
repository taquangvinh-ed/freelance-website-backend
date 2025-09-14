package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class TestimonialsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testimonial_id;

    private String rating;
    private String comment;

    // The date that the testimonial was given
    private String date;


    private Boolean testiminial_back;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerTestimonials;

    @ManyToOne
    @JoinColumn(name="project_id" )
    private ProjectsModel projectTestimonials;

    @ManyToOne
    @JoinColumn(name="client_id" )
    private ClientsModel clientTestimonials;

    @ManyToOne
    @JoinColumn(name="company_id" )
    private CompaniesModel companyTestimonials;

    @ManyToOne
    @JoinColumn(name="team_id" )
    private TeamsModel teamTestimonials;

    public TestimonialsModel() {
    }

    public Long getTestimonial_id() {
        return testimonial_id;
    }

    public void setTestimonial_id(Long testimonial_id) {
        this.testimonial_id = testimonial_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getTestiminial_back() {
        return testiminial_back;
    }

    public void setTestiminial_back(Boolean testiminial_back) {
        this.testiminial_back = testiminial_back;
    }

    public FreelancersModel getFreelancerTestimonials() {
        return freelancerTestimonials;
    }

    public void setFreelancerTestimonials(FreelancersModel freelancerTestimonials) {
        this.freelancerTestimonials = freelancerTestimonials;
    }

    public ProjectsModel getProjectTestimonials() {
        return projectTestimonials;
    }

    public void setProjectTestimonials(ProjectsModel projectTestimonials) {
        this.projectTestimonials = projectTestimonials;
    }

    public ClientsModel getClientTestimonials() {
        return clientTestimonials;
    }

    public void setClientTestimonials(ClientsModel clientTestimonials) {
        this.clientTestimonials = clientTestimonials;
    }

    public CompaniesModel getCompanyTestimonials() {
        return companyTestimonials;
    }

    public void setCompanyTestimonials(CompaniesModel companyTestimonials) {
        this.companyTestimonials = companyTestimonials;
    }

    public TeamsModel getTeamTestimonials() {
        return teamTestimonials;
    }

    public void setTeamTestimonials(TeamsModel teamTestimonials) {
        this.teamTestimonials = teamTestimonials;
    }
}
