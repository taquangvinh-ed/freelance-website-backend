package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Set;

@Entity
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



    public TestsModel() {
    }

    public Long getTest_id() {
        return test_id;
    }

    public void setTest_id(Long test_id) {
        this.test_id = test_id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Double getPassing_score() {
        return passing_score;
    }

    public void setPassing_score(Double passing_score) {
        this.passing_score = passing_score;
    }

    public String getCertificate_url() {
        return certificate_url;
    }

    public void setCertificate_url(String certificate_url) {
        this.certificate_url = certificate_url;
    }

    public FreelancersModel getFreelancerTests() {
        return freelancerTests;
    }

    public void setFreelancerTests(FreelancersModel freelancerTests) {
        this.freelancerTests = freelancerTests;
    }

    public Set<SkillsModel> getSkillsTests() {
        return skillsTests;
    }

    public void setSkillsTests(Set<SkillsModel> skillsTests) {
        this.skillsTests = skillsTests;
    }
}
