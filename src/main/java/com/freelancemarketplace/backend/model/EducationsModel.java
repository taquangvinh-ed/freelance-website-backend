package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EducationsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long education_id;

    private String organization;
    private String degree;
    private String area_study;
    private String start_date;
    private String end_date;
    private String description;



    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerEducations;

}
