package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.model.Bio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class ClientDTO {
    private Long clientId;

    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String profilePicture;
    private Bio bio;
    private Boolean isVerified;
    private Boolean isBlocked;

  //  private Set<LanguageDTO> languages;


//    private AnalyticModel analytic;


   // private LocationDTO location;

//    private Set<TestimonialModel> testimonials;





    //Products bought by the client
//    private Set<ProductModel> products;

    private String stripeCustomerId;
}
