package com.freelancemarketplace.backend.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Set;

@Entity
public class CompaniesModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_id;

    private String name;
    private String description;
    private String website;
    private Boolean isVerified;
    private Boolean isBlocked;
    private String logoUrl;
    private String email;
    private String password_hash;
    private String picture_url;
    private String wallet;
    private String phone_number;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String social_media_links; // JSON string to hold various social media links

    @OneToOne
    @JoinColumn(name = "analytics_id")
    private AnalyticsModel company_analytics;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationsModel company_location;

    @OneToMany(mappedBy = "companyTestimonials")
    private Set<TestimonialsModel> testimonials;

    @OneToMany(mappedBy = "companyPayments")
    private Set<PaymentsModel> paymentsCompany;

    @OneToMany(mappedBy = "contractCompany")
    private Set<ContractsModel> companyContracts;

    @OneToMany(mappedBy = "companyMessages")
    private Set<MessagesModel> messages;

    @OneToMany(mappedBy = "companyReports")
    private Set<ReportsModel> reportsList;

    @ManyToMany
    @JoinTable(
            name = "company_products",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<ProductsModel> productsList;

    @OneToMany(mappedBy = "projectCompany")
    private Set<ProjectsModel> projectsList;

    public CompaniesModel(Long company_id, String name, String description, String website, Boolean isVerified, Boolean isBlocked, String logoUrl, String email, String password_hash, String picture_url, String wallet, String phone_number, String social_media_links, AnalyticsModel company_analytics, LocationsModel company_location, Set<TestimonialsModel> testimonials, Set<PaymentsModel> paymentsCompany, Set<ContractsModel> companyContracts, Set<MessagesModel> messages, Set<ReportsModel> reportsList, Set<ProductsModel> productsList, Set<ProjectsModel> projectsList) {
        this.company_id = company_id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.isVerified = isVerified;
        this.isBlocked = isBlocked;
        this.logoUrl = logoUrl;
        this.email = email;
        this.password_hash = password_hash;
        this.picture_url = picture_url;
        this.wallet = wallet;
        this.phone_number = phone_number;
        this.social_media_links = social_media_links;
        this.company_analytics = company_analytics;
        this.company_location = company_location;
        this.testimonials = testimonials;
        this.paymentsCompany = paymentsCompany;
        this.companyContracts = companyContracts;
        this.messages = messages;
        this.reportsList = reportsList;
        this.productsList = productsList;
        this.projectsList = projectsList;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getSocial_media_links() {
        return social_media_links;
    }

    public void setSocial_media_links(String social_media_links) {
        this.social_media_links = social_media_links;
    }

    public AnalyticsModel getCompany_analytics() {
        return company_analytics;
    }

    public void setCompany_analytics(AnalyticsModel company_analytics) {
        this.company_analytics = company_analytics;
    }

    public LocationsModel getCompany_location() {
        return company_location;
    }

    public void setCompany_location(LocationsModel company_location) {
        this.company_location = company_location;
    }

    public Set<TestimonialsModel> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(Set<TestimonialsModel> testimonials) {
        this.testimonials = testimonials;
    }

    public Set<PaymentsModel> getPaymentsCompany() {
        return paymentsCompany;
    }

    public void setPaymentsCompany(Set<PaymentsModel> paymentsCompany) {
        this.paymentsCompany = paymentsCompany;
    }

    public Set<ContractsModel> getCompanyContracts() {
        return companyContracts;
    }

    public void setCompanyContracts(Set<ContractsModel> companyContracts) {
        this.companyContracts = companyContracts;
    }

    public Set<MessagesModel> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessagesModel> messages) {
        this.messages = messages;
    }

    public Set<ReportsModel> getReportsList() {
        return reportsList;
    }

    public void setReportsList(Set<ReportsModel> reportsList) {
        this.reportsList = reportsList;
    }

    public Set<ProductsModel> getProductsList() {
        return productsList;
    }

    public void setProductsList(Set<ProductsModel> productsList) {
        this.productsList = productsList;
    }

    public Set<ProjectsModel> getProjectsList() {
        return projectsList;
    }

    public void setProjectsList(Set<ProjectsModel> projectsList) {
        this.projectsList = projectsList;
    }
}
