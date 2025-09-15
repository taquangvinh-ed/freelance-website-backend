package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @JoinColumn(name = "team_id")
    private TeamsModel team;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectsModel project;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductsModel product;


}
