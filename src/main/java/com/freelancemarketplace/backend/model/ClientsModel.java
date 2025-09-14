package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class ClientsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;

    @ManyToMany
    @JoinTable(
            name = "Client_Language",
            joinColumns = @JoinColumn (name="client_id"),
            inverseJoinColumns = @JoinColumn(name="language_id")
    )
    private Set<LanguagesModel> client_languages;

    @OneToOne
    @JoinColumn(name = "analytics_id")
    private AnalyticsModel client_analytics;

    @ManyToOne
    @JoinColumn(name="location_id")
    private LocationsModel client_location;

    @OneToMany(mappedBy = "clientTestimonials" )
    private Set<TestimonialsModel> testimonials;


    @OneToMany(mappedBy = "clientPayments")
    private Set<PaymentsModel> paymentsClient;

    @OneToMany(mappedBy = "contractClient")
    private Set<ContractsModel> clientContracts;

}
