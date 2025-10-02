package com.freelancemarketplace.backend.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    private String usernmame;

    private String email;

    private String password;
}
