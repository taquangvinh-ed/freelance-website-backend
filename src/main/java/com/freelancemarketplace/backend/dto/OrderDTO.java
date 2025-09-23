package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.OrderStatus;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.ProductModel;
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

    private Long timeOfDownload;
}
