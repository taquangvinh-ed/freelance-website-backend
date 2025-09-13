package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class PaymentsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payment_id;

    //The payments that belong to a freelancer
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerPayments;
}
