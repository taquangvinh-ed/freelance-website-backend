package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class CertificatesModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificate_id;

    private String title;
    private String organization;
    private String date_obtained;
    private String certificate_url;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerCertificates;

    public CertificatesModel() {
    }

    public Long getCertificate_id() {
        return certificate_id;
    }

    public void setCertificate_id(Long certificate_id) {
        this.certificate_id = certificate_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDate_obtained() {
        return date_obtained;
    }

    public void setDate_obtained(String date_obtained) {
        this.date_obtained = date_obtained;
    }

    public String getCertificate_url() {
        return certificate_url;
    }

    public void setCertificate_url(String certificate_url) {
        this.certificate_url = certificate_url;
    }

    public FreelancersModel getFreelancerCertificates() {
        return freelancerCertificates;
    }

    public void setFreelancerCertificates(FreelancersModel freelancerCertificates) {
        this.freelancerCertificates = freelancerCertificates;
    }
}
