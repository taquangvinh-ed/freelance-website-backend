package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectProposalDTO {

   private String imageUrl;

   private String firstName;

   private String lastName;

   private String username;

   private double avarageRating;

   private String proposalDescription;

}
