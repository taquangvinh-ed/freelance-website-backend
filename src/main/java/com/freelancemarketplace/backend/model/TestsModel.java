package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

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

//    @Type(JsonBinaryType.class)
//    @Column(columnDefinition = "jsonb")
//    private Question questions; //JSON string representing questions
//
//    @Type(JsonBinaryType.class)
//    @Column(columnDefinition = "jsonb")
//    private Answer answers; //JSON string representing answers

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
    private Set<SkillsModel> skillsTests;

}
