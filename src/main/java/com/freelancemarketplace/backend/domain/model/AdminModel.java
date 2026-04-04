package com.freelancemarketplace.backend.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.freelancer.domain.model.Bio;
import com.freelancemarketplace.backend.location.domain.model.LocationModel;
import com.freelancemarketplace.backend.notification.domain.model.NotificationModel;
import com.freelancemarketplace.backend.user.domain.model.UserModel;

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
