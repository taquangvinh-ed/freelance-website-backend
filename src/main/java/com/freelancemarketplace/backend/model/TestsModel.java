package com.freelancemarketplace.backend.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity
public class TestsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long test_id;

    private  Integer duration; //in minutes
    private String title;
    private String description;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String questions; //JSON string representing questions

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String answers; //JSON string representing answers

    private Double passing_score; //e.g., 70.0 for 70%

    private String certificate_url;

    @ManyToOne
    @JoinTable(
        name = "freelancer_tests",
        joinColumns = @JoinColumn(name = "test_id"),
        inverseJoinColumns = @JoinColumn(name = "freelancer_id")
    )
    private FreelancersModel freelancerTests;

    @ManyToMany
    @JoinTable(
        name = "test_questions",
        joinColumns = @JoinColumn(name = "test_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private SkillsModel skillsTests;


}
