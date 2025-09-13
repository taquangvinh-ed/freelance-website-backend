package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class TestimonialsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testimonial_id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerTestimonials;
}
