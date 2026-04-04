package com.freelancemarketplace.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import com.freelancemarketplace.backend.admin.domain.model.QuestionModel;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerTestResults;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Tests")
public class TestModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    private  Integer duration; //in minutes
    private String title;
    private String description;


    @OneToMany(mappedBy = "test")
    private Set<QuestionModel> questions;

    private Double passingScore; //e.g., 70.0 for 70%


    private String certificateUrl;

//    @ManyToMany
//    @JoinTable(
//        name = "freelancer_tests",
//        joinColumns = @JoinColumn(name = "testId"),
//        inverseJoinColumns = @JoinColumn(name = "freelancerId")
//    )
//    private Set<FreelancerModel> freelancers;

    @ManyToMany
    @JoinTable(
        name = "test_skills",
        joinColumns = @JoinColumn(name = "testId"),
        inverseJoinColumns = @JoinColumn(name = "skillId")
    )
    private Set<SkillModel> skills;

    @OneToMany(mappedBy = "test")
    private Set<FreelancerTestResults> testResults;

}
