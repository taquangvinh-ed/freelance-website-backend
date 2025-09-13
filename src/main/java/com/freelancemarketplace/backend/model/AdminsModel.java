package com.freelancemarketplace.backend.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;

import java.util.Set;

@Entity
public class AdminsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long admin_id;

    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String password_hash;
    private String phone_number;
    private String profile_picture_url;
    private String title;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String bio;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String social_media_links;




    @OneToMany(mappedBy = "admin")
    private Set<QandAModel> questions_and_answers;

    @OneToMany(mappedBy = "admin")
    private Set<Notifications> notifications;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    private LocationsModel admin_location;

    public AdminsModel(Long admin_id, String first_name, String last_name, String username, String email, String password_hash, String phone_number, String profile_picture_url, String title, String bio, String social_media_links, Set<QandAModel> questions_and_answers, Set<Notifications> notifications, LocationsModel admin_location) {
        this.admin_id = admin_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.phone_number = phone_number;
        this.profile_picture_url = profile_picture_url;
        this.title = title;
        this.bio = bio;
        this.social_media_links = social_media_links;
        this.questions_and_answers = questions_and_answers;
        this.notifications = notifications;
        this.admin_location = admin_location;
    }

    public AdminsModel() {
    }

    public Long getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(Long admin_id) {
        this.admin_id = admin_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSocial_media_links() {
        return social_media_links;
    }

    public void setSocial_media_links(String social_media_links) {
        this.social_media_links = social_media_links;
    }

    public Set<QandAModel> getQuestions_and_answers() {
        return questions_and_answers;
    }

    public void setQuestions_and_answers(Set<QandAModel> questions_and_answers) {
        this.questions_and_answers = questions_and_answers;
    }

    public Set<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notifications> notifications) {
        this.notifications = notifications;
    }

    public LocationsModel getAdmin_location() {
        return admin_location;
    }

    public void setAdmin_location(LocationsModel admin_location) {
        this.admin_location = admin_location;
    }
}
