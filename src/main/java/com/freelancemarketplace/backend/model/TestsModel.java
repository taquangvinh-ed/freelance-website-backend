package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TestsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long test_id;

    private  Integer duration; //in minutes
    private String title;
    private String description;


    @OneToMany(mappedBy = "test")
    private Set<QuestionsModel> questions;

    private Double passing_score; //e.g., 70.0 for 70%


    private String certificate_url;

    @ManyToMany
    @JoinTable(
        name = "freelancer_tests",
        joinColumns = @JoinColumn(name = "test_id"),
        inverseJoinColumns = @JoinColumn(name = "freelancer_id")
    )
    private Set<FreelancersModel> freelancerTests;

    @ManyToMany
    @JoinTable(
        name = "test_skills",
        joinColumns = @JoinColumn(name = "test_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillsModel> skillsTests;

    @OneToMany(mappedBy = "test")
    private Set<FreelancerTestResults> testResults;

}
