package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class ProductsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

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
