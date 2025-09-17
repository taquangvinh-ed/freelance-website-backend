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
@Table(name = "Companies")
public class CompanyModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    private String name;

    private String website;
    private Boolean isVerified;
    private Boolean isBlocked;
    private String logoUrl;
    private String email;
    private String passwordHash;
    private String pictureUrl;
    private String wallet;
    private String phoneNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Bio bio;

    @OneToOne
    @JoinColumn(name = "analyticsId")
    private AnalyticModel analytic;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private LocationModel location;

    @OneToMany(mappedBy = "company")
    private Set<TestimonialModel> testimonials;

    @OneToMany(mappedBy = "company")
    private Set<PaymentModel> payments;

    @OneToMany(mappedBy = "company")
    private Set<ContractModel> contracts;

    @OneToMany(mappedBy = "company")
    private Set<MessageModel> messages;

    @OneToMany(mappedBy = "company")
    private Set<ReportModel> reports;

    @ManyToMany
    @JoinTable(
            name = "company_products",
            joinColumns = @JoinColumn(name = "companyId"),
            inverseJoinColumns = @JoinColumn(name = "productId")
    )
    private Set<ProductModel> products;

    @OneToMany(mappedBy = "company")
    private Set<ProjectModel> projects;

}
