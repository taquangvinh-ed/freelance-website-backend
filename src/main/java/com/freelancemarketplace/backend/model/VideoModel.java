package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Videos")
public class VideoModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(nullable = false)
    private String url;

    private Integer duration;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private TeamModel team;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private ProjectModel project;

    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductModel product;


}
