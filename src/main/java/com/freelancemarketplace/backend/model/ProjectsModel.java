package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ProjectsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;


    @OneToMany(mappedBy = "project")
    private List<VideosModel> videos;

    @OneToOne(mappedBy = "project")
    private PortfoliosModel portfolio;
}
