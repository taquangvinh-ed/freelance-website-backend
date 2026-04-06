package com.freelancemarketplace.backend.certification.application.service;

import com.freelancemarketplace.backend.certification.dto.CertificateDTO;

import java.util.List;

public interface CertificateService {
    CertificateDTO createCertificate(Long freelancerId, CertificateDTO certificateDTO);
    CertificateDTO updateCertificate(Long certificateId, CertificateDTO certificateDTO);
    void deleteCertificate(Long certificateId);
    List<CertificateDTO> getAllCertificateByFreelancer(Long freelancerId);
}
