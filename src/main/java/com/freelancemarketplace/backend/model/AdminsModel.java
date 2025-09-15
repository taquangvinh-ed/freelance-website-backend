package com.freelancemarketplace.backend.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
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
public class AdminsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String profilePictureUrl;
    private String title;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Bio bio;


    @OneToMany(mappedBy = "admin")
    private Set<QandAModel> questionsAndAnswers;

    @OneToMany(mappedBy = "admin")
    private Set<NotificationsModel> notifications;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    private LocationsModel location;


}
