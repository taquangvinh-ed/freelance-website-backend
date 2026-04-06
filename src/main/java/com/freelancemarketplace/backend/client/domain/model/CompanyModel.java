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
import com.freelancemarketplace.backend.location.domain.model.LocationModel;
import com.freelancemarketplace.backend.payment.domain.model.PaymentModel;
import com.freelancemarketplace.backend.product.domain.model.ProductModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.report.domain.model.ReportModel;

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
    private String username;
    private String passwordHash;
    private String role = "EMPLOYER";
    private String pictureUrl;
    private String wallet;
    private String phoneNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Bio bio;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private LocationModel location;


    @OneToMany(mappedBy = "company")
    private Set<PaymentModel> payments;

    @OneToMany(mappedBy = "company")
    private Set<ContractModel> contracts;

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
