package com.freelancemarketplace.backend.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Admin")
public class AdminModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @NotBlank(message = "First name must not be null")
    private String firstName;

    @NotBlank(message = "Last name must not be null")
    private String lastName;

    @NotBlank(message = "username must not be null")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(unique = true)
    private String email;

    private String passwordHash;

    @Column(unique = true)
    private String phoneNumber;

    private String profilePictureUrl;

    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Bio bio;


    @OneToMany(mappedBy = "admin")
    private Set<QandAModel> questionsAndAnswers;

    @OneToMany(mappedBy = "admin")
    private Set<NotificationModel> notifications;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private LocationModel location;


}
