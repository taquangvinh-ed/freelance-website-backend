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
public class CompaniesModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_id;

    private String name;

    private String website;
    private Boolean isVerified;
    private Boolean isBlocked;
    private String logoUrl;
    private String email;
    private String password_hash;
    private String picture_url;
    private String wallet;
    private String phone_number;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> bio;

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

    @OneToMany(mappedBy = "companyReports")
    private Set<ReportsModel> reportsList;

    @ManyToMany
    @JoinTable(
            name = "company_products",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<ProductsModel> productsList;

    @OneToMany(mappedBy = "projectCompany")
    private Set<ProjectsModel> projectsList;

}
