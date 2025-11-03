package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.CertificateDTO;

import java.util.List;

public interface CertificateService {
    CertificateDTO createCertificate(Long freelancerId, CertificateDTO certificateDTO);
    CertificateDTO updateCertificate(Long certificateId, CertificateDTO certificateDTO);
    void deleteCertificate(Long certificateId);
    List<CertificateDTO> getAllCertificateByFreelancer(Long freelancerId);
}
