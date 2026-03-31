package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

    private Long orderId;


    private Long clientId;


    private Long productId;

    private Double price;

    private Timestamp purchaseDate;


    private String status;

    private String redirectUrl;

}
