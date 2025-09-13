package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class LocationsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long location_id;

    @OneToMany(mappedBy = "admin_location")
    private Set<AdminsModel> adminList;
}
