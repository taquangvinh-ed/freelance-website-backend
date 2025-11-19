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

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Admin")
public class AdminModel extends BaseEntity{
    @Id
    private Long adminId;

    @NotBlank(message = "First name must not be null")
    private String firstName;

    @NotBlank(message = "Last name must not be null")
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    private String profilePictureUrl;

    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

//    @Type(JsonBinaryType.class)
//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(columnDefinition = "jsonb")
//    private Bio bio;


    @OneToMany(mappedBy = "admin")
    private Set<Q_AModel> questionsAndAnswers;

    @OneToMany(mappedBy = "admin")
    private Set<NotificationModel> notifications;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private LocationModel location;

    @OneToOne
    @JoinColumn(name = "userId")
    private UserModel user;

    @OneToMany(mappedBy = "disableBy")
    private List<UserModel> userModel;

}
