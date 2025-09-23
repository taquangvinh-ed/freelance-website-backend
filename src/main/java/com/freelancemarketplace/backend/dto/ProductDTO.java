package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    private Long productId;

    private String name;
    private String description;
    private Double price;

    private String ownerType; // freelancer or team

    private String buyerType; // individual or company

    private String image;

    // Link to the product (e.g., URL to download or view the product)
    private String productLink;

    private Long views;

    private Long timeOfDownload;

    //The freelancer who creates the product
    private Long freelancerId;

    private Set<Long> skillIds = new HashSet<>();
}
