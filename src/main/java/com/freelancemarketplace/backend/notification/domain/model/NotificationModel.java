package com.freelancemarketplace.backend.notification.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import com.freelancemarketplace.backend.admin.domain.model.AdminModel;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Notifications")
public class NotificationModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private String title;

    private String description;

    private Timestamp datetime;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    private AdminModel admin;

}
