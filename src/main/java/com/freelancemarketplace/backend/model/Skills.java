package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Skills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skill_id;

    @ManyToMany(mappedBy = "skills")
    private Set<PortfoliosModel> portfolios;
}
