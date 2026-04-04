package com.freelancemarketplace.backend.certification.dto;

import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {
    private Long certificateId;
    private String title;
    private String organization;
    private String dateObtained;
    private String certificateUrl;
    private Long freelancerId;

}
