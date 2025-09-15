package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

}
