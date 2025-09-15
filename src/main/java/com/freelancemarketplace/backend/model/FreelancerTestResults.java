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
    private Long test_result_id;


    private Double score;
    private Timestamp completionDate;
    private boolean isPassed;

    //Freelancer who take the test
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancer;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private TestsModel test;
}
