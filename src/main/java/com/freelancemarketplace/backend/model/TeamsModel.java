package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class TeamsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    @OneToMany(mappedBy = "team")
    private List<VideosModel> videos;
}
