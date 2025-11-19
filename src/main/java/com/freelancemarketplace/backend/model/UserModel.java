package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.enums.AccountStatus;
import com.freelancemarketplace.backend.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Timestamp;
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
public class UserModel extends BaseEntity implements AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRoles role; // e.g., "FREELANCER", "CLIENT", "ADMIN"


    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(columnDefinition = "TEXT")
    private String disableReason;

    private Timestamp disableAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    private AdminModel disableBy;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private FreelancerModel freelancer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ClientModel client;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private AdminModel admin;

    @Override
    public Long getId() {
        return this.userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.toString()));
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

    @Override
    public boolean isEnabled() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked(){
        return accountStatus != AccountStatus.DISABLED && accountStatus != AccountStatus.BANNED;
    }
}
