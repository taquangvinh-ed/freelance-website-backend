package com.freelancemarketplace.backend.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Set;

@Entity
public class ClientsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;

    private String first_name;
    private String last_name;
    private String email;
    private String password_hash;
    private String phone_number;
    private String profile_picture;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String description;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String social_links;

    private Boolean is_verified;
    private Boolean is_blocked;



    @ManyToMany
    @JoinTable(
            name = "Client_Language",
            joinColumns = @JoinColumn (name="client_id"),
            inverseJoinColumns = @JoinColumn(name="language_id")
    )
    private Set<LanguagesModel> client_languages;

    @OneToOne
    @JoinColumn(name = "analytics_id")
    private AnalyticsModel client_analytics;

    @ManyToOne
    @JoinColumn(name="location_id")
    private LocationsModel client_location;

    @OneToMany(mappedBy = "clientTestimonials" )
    private Set<TestimonialsModel> testimonials;

    @OneToMany(mappedBy = "clientPayments")
    private Set<PaymentsModel> paymentsClient;

    @OneToMany(mappedBy = "contractClient")
    private Set<ContractsModel> clientContracts;

    @OneToMany(mappedBy = "clientMessages")
    private Set<MessagesModel> messages;

    @OneToMany(mappedBy = "projectClient" )
    private Set<ProjectsModel> projects;

    //Reports made by the client
    @OneToMany(mappedBy = "client_reports" )
    private Set<ReportsModel> reports;

    //Products bought by the client
    @OneToMany(mappedBy = "clientProduct" )
    private Set<ProductsModel> products;


    public ClientsModel(Long client_id, String first_name, String last_name, String email, String password_hash, String phone_number, String profile_picture, String description, String social_links, Boolean is_verified, Boolean is_blocked, Set<LanguagesModel> client_languages, AnalyticsModel client_analytics, LocationsModel client_location, Set<TestimonialsModel> testimonials, Set<PaymentsModel> paymentsClient, Set<ContractsModel> clientContracts, Set<MessagesModel> messages, Set<ProjectsModel> projects, Set<ReportsModel> reports, Set<ProductsModel> products) {
        this.client_id = client_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password_hash = password_hash;
        this.phone_number = phone_number;
        this.profile_picture = profile_picture;
        this.description = description;
        this.social_links = social_links;
        this.is_verified = is_verified;
        this.is_blocked = is_blocked;
        this.client_languages = client_languages;
        this.client_analytics = client_analytics;
        this.client_location = client_location;
        this.testimonials = testimonials;
        this.paymentsClient = paymentsClient;
        this.clientContracts = clientContracts;
        this.messages = messages;
        this.projects = projects;
        this.reports = reports;
        this.products = products;
    }

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
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

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSocial_links() {
        return social_links;
    }

    public void setSocial_links(String social_links) {
        this.social_links = social_links;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(Boolean is_verified) {
        this.is_verified = is_verified;
    }

    public Boolean getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(Boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    public Set<LanguagesModel> getClient_languages() {
        return client_languages;
    }

    public void setClient_languages(Set<LanguagesModel> client_languages) {
        this.client_languages = client_languages;
    }

    public AnalyticsModel getClient_analytics() {
        return client_analytics;
    }

    public void setClient_analytics(AnalyticsModel client_analytics) {
        this.client_analytics = client_analytics;
    }

    public LocationsModel getClient_location() {
        return client_location;
    }

    public void setClient_location(LocationsModel client_location) {
        this.client_location = client_location;
    }

    public Set<TestimonialsModel> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(Set<TestimonialsModel> testimonials) {
        this.testimonials = testimonials;
    }

    public Set<PaymentsModel> getPaymentsClient() {
        return paymentsClient;
    }

    public void setPaymentsClient(Set<PaymentsModel> paymentsClient) {
        this.paymentsClient = paymentsClient;
    }

    public Set<ContractsModel> getClientContracts() {
        return clientContracts;
    }

    public void setClientContracts(Set<ContractsModel> clientContracts) {
        this.clientContracts = clientContracts;
    }

    public Set<MessagesModel> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessagesModel> messages) {
        this.messages = messages;
    }

    public Set<ProjectsModel> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectsModel> projects) {
        this.projects = projects;
    }

    public Set<ReportsModel> getReports() {
        return reports;
    }

    public void setReports(Set<ReportsModel> reports) {
        this.reports = reports;
    }

    public Set<ProductsModel> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductsModel> products) {
        this.products = products;
    }
}
