package com.freelancemarketplace.backend.auth;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomUsernamePasswordAuthenticationProvider(CustomUserDetailService customUserDetailsService,
                                                        PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if(password.isEmpty())
            throw new BadCredentialsException("No password provided");

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(!userDetails.isEnabled())
            throw new DisabledException("User account is disable");

        if(!userDetails.isAccountNonLocked())
            throw new LockedException("User account is locked");

        if(passwordEncoder.matches(password, userDetails.getPassword())){
            return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid password");
        }


 }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
