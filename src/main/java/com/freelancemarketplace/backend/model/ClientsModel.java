package com.freelancemarketplace.backend.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClientsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;

    private String first_name;
    private String last_name;
    private String email;
    private String password_hash;
    private String phone_number;
    private String profile_picture;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Bio bio;

    private Boolean is_verified;
    private Boolean is_blocked;



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

    @OneToMany(mappedBy = "clientMessages")
    private Set<MessagesModel> messages;

    @OneToMany(mappedBy = "projectClient" )
    private Set<ProjectsModel> projects;

    //Reports made by the client
    @OneToMany(mappedBy = "client_reports" )
    private Set<ReportsModel> reports;

    //Products bought by the client
    @OneToMany(mappedBy = "clientProduct" )
    private Set<ProductsModel> products;


}
