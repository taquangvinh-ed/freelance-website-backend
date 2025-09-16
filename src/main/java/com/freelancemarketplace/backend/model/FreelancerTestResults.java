package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FreelancerTestResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testResultId;


    private Double score;
    private Timestamp completionDate;
    private boolean isPassed;

    //Freelancer who take the test
    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne
    @JoinColumn(name = "testId")
    private TestModel test;
}
