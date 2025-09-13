package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class CompaniesModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_id;

    @OneToOne
    @JoinColumn(name = "analytics_id")
    private AnalyticsModel company_analytics;
}
