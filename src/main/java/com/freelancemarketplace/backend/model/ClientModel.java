package com.freelancemarketplace.backend.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Clients")
public class ClientModel extends BaseEntity{
    @Id
    private Long clientId;

    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;

    private String username;
    private String passwordHash;
    private String role = "EMPLOYER";
    @Column(unique = true)
    private String phoneNumber;
    private String profilePicture;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Bio bio;

    private Boolean isVerified;
    private Boolean isBlocked;
    private String avatar;



    @ManyToMany
    @JoinTable(
            name = "Client_Language",
            joinColumns = @JoinColumn (name="clientId"),
            inverseJoinColumns = @JoinColumn(name="languageId")
    )
    private Set<LanguageModel> languages;

    @OneToOne
    @JoinColumn(name = "analyticId")
    private AnalyticModel analytic;

    @ManyToOne
    @JoinColumn(name="locationId")
    private LocationModel location;

    @OneToMany(mappedBy = "client")
    private Set<TestimonialModel> testimonials;

    @OneToMany(mappedBy = "client")
    private Set<PaymentModel> payments;

    @OneToMany(mappedBy = "client")
    private Set<ContractModel> contracts;


    @OneToMany(mappedBy = "client")
    private Set<ProjectModel> projects;

    //Reports made by the client
    @OneToMany(mappedBy = "client")
    private Set<ReportModel> reports;

    //Products bought by the client
    @ManyToMany
    @JoinTable(
            name = "client_products",
            joinColumns = @JoinColumn(name = "clientId"),
            inverseJoinColumns = @JoinColumn(name = "productId")
    )
    private Set<ProductModel> products;

    @OneToOne
    @JoinColumn(name = "userId")
    private UserModel user;

    private String stripeCustomerId;


}
