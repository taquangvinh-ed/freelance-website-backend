package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.RecuiterTypes;
import com.freelancemarketplace.backend.enums.TailentTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    private String name;
    private String description;
    private Double price;

    @Enumerated(EnumType.STRING)
    private TailentTypes owner_type; // freelancer or team

    @Enumerated(EnumType.STRING)
    private RecuiterTypes buyer_type; // individual or company

    private String image;

    // Link to the product (e.g., URL to download or view the product)
    private String product_link;

    private Long views;
    private Long time_of_download;

    @OneToMany(mappedBy = "product")
    private Set<VideosModel> videos;

    //The freelancer who creates the product
    @ManyToOne
    @JoinColumn(name="freelancer_id")
    private FreelancersModel freelancerProduct;

    @OneToMany(mappedBy = "productMessages")
    private Set<MessagesModel> messages;

    @ManyToMany(mappedBy = "productsList" )
    private Set<CompaniesModel> companyList;

    @ManyToMany
    @JoinTable(
            name = "product_skills",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillsModel> skills;

    //The client who buys the product
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientsModel clientProduct;

}
