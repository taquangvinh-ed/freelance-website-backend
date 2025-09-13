package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class AdminsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long admin_id;

    @OneToMany(mappedBy = "admin")
    private Set<QandAModel> questions_and_answers;

    @OneToMany(mappedBy = "admin")
    private Set<Notifications> notifications;


}
