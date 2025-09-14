package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
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

    public NotificationsModel() {
    }

    public Long getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(Long notification_id) {
        this.notification_id = notification_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public AdminsModel getAdmin() {
        return admin;
    }

    public void setAdmin(AdminsModel admin) {
        this.admin = admin;
    }
}
