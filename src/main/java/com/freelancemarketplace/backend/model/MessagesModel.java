package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class MessagesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long message_id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerMessages;
}
