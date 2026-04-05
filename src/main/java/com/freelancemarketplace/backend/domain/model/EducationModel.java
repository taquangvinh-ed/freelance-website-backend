package com.freelancemarketplace.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;

@Entity(name = "LegacyEducationModel")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Educations")
public class EducationModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;

    private String organization;
    private String degree;
    private String areaStudy;
    private String startDate;
    private String endDate;
    private String description;



    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

}
