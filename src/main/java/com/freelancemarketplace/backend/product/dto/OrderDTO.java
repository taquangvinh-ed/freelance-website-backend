package com.freelancemarketplace.backend.product.dto;

import com.freelancemarketplace.backend.product.domain.enums.OrderStatus;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.product.domain.model.ProductModel;
import jakarta.persistence.*;
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
