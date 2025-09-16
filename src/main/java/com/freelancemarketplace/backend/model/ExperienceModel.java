package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Experiences")
public class ExperienceModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    private String title;
    private String organization;
    private String startDate;
    private String endDate;
    private String description;


    @ManyToOne
    @JoinColumn(name = "locationId")
    private LocationModel location;

    //The experiences that belong to a freelancer
    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

}
