package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.RecuiterTypes;
import com.freelancemarketplace.backend.enums.TailentTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Products")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;
    private String description;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private TailentTypes ownerType; // freelancer or team

    @Enumerated(EnumType.STRING)
    private RecuiterTypes buyerType; // individual or company

    private String image;

    // Link to the product (e.g., URL to download or view the product)
    private String productLink;

    private Long views;

    private Long timeOfDownload;

    @OneToMany(mappedBy = "product")
    private Set<VideoModel> videos;

    //The freelancer who creates the product
    @ManyToOne
    @JoinColumn(name="freelancerId")
    private FreelancerModel freelancer;


    //The companies that bought the product
    @ManyToMany(mappedBy = "products")
    private Set<CompanyModel> companies;

    @ManyToMany
    @JoinTable(
            name = "product_skills",
            joinColumns = @JoinColumn(name = "productId"),
            inverseJoinColumns = @JoinColumn(name = "skillId")
    )
    private Set<SkillModel> skills = new HashSet<>();

    //The client who buys the product
    @ManyToMany(mappedBy = "products")
    private Set<ClientModel> clients;

}
