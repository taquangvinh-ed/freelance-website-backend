package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ProductsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @OneToMany(mappedBy = "product")
    private List<VideosModel> videos;

    //The freelancer who creates the product
    @ManyToOne
    @JoinColumn(name="freelancer_id")
    private FreelancersModel freelancerProduct;
}
