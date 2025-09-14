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
}
