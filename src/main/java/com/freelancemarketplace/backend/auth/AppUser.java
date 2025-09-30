package com.freelancemarketplace.backend.auth;

import com.freelancemarketplace.backend.enums.UserTypes;
import org.springframework.security.core.userdetails.UserDetails;

public interface AppUser extends UserDetails {
   public Long getId();
   public String getEmail();
   public UserTypes getUserType();
}
