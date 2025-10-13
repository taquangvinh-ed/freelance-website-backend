package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProjectProposalDTO {

   private Long id;

   private String imageUrl;

   private Long freelancerId;

   private String firstName;

   private String lastName;

   private String username;

   private double avarageRating;

   private String proposalDescription;

   private BigDecimal amount;

   private Integer deliveryDays;


}
