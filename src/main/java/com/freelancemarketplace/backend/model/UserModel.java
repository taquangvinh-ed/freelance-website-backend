package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", indexes = {
        @Index(name = "idx_user_username", columnList = "username"),
        @Index(name = "idx_user_email", columnList = "email")
})
public class UserModel implements AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRoles role; // e.g., "FREELANCER", "CLIENT", "ADMIN"

    private Boolean isVerified;
    private Boolean isBlocked;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private FreelancerModel freelancer;


    @Override
    public Long getId() {
        return this.userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role));
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
    public String getEmail(){
        return this.email;
    }
}
