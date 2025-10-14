package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrentUserProfileDTO {

    private String firstName;
    private String lastName;
    private String role;
    private String avatar;

}
