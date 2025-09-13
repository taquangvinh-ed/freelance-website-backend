package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class TestsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long test_id;

    @ManyToOne
    @JoinTable(
        name = "freelancer_tests",
        joinColumns = @JoinColumn(name = "test_id"),
        inverseJoinColumns = @JoinColumn(name = "freelancer_id")
    )
    private FreelancersModel freelancerTests;
}
