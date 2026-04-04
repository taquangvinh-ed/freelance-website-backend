package com.freelancemarketplace.backend.client.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.freelancer.domain.model.Bio;
import com.freelancemarketplace.backend.language.domain.model.LanguageModel;
import com.freelancemarketplace.backend.location.domain.model.LocationModel;
import com.freelancemarketplace.backend.payment.domain.model.PaymentModel;
import com.freelancemarketplace.backend.product.domain.model.ProductModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.report.domain.model.ReportModel;
import com.freelancemarketplace.backend.review.domain.model.TestimonialModel;
import com.freelancemarketplace.backend.user.domain.model.UserModel;

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
