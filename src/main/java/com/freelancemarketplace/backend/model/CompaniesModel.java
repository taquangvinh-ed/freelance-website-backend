package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class CompaniesModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_id;

    @OneToOne
    @JoinColumn(name = "analytics_id")
    private AnalyticsModel company_analytics;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationsModel company_location;

    @OneToMany(mappedBy = "companyTestimonials")
    private Set<TestimonialsModel> testimonials;

    @OneToMany(mappedBy = "companyPayments")
    private Set<PaymentsModel> paymentsCompany;

    @OneToMany(mappedBy = "contractCompany")
    private Set<ContractsModel> companyContracts;

    @OneToMany(mappedBy = "companyMessages")
    private Set<MessagesModel> messages;
}
