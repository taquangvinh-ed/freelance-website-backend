package com.freelancemarketplace.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Certificates")
public class CertificateModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    private String title;
    private String organization;
    private String dateObtained;
    private String certificateUrl;

    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

}
