package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class VideosModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long video_id;

    @Column(nullable = false)
    private String video_url;

    private Integer video_duration;

    private String video_title;

    private String video_description;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private TeamsModel team;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private ProjectsModel project;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = true)
    private ProjectsModel freelancer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    private ProjectsModel product;

    public VideosModel(Long video_id, String video_url, Integer video_duration, String video_title, String video_description, TeamsModel team, ProjectsModel project, ProjectsModel freelancer, ProjectsModel product) {
        this.video_id = video_id;
        this.video_url = video_url;
        this.video_duration = video_duration;
        this.video_title = video_title;
        this.video_description = video_description;
        this.team = team;
        this.project = project;
        this.freelancer = freelancer;
        this.product = product;
    }

    public Long getVideo_id() {
        return video_id;
    }

    public void setVideo_id(Long video_id) {
        this.video_id = video_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public Integer getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(Integer video_duration) {
        this.video_duration = video_duration;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public TeamsModel getTeam() {
        return team;
    }

    public void setTeam(TeamsModel team) {
        this.team = team;
    }

    public ProjectsModel getProject() {
        return project;
    }

    public void setProject(ProjectsModel project) {
        this.project = project;
    }

    public ProjectsModel getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(ProjectsModel freelancer) {
        this.freelancer = freelancer;
    }

    public ProjectsModel getProduct() {
        return product;
    }

    public void setProduct(ProjectsModel product) {
        this.product = product;
    }
}
