package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class NotificationsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    private String title;

    private String description;

    private Timestamp datetime;

    private String image_url;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private AdminsModel admin;

}
