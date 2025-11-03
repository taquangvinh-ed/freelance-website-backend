package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.CertificateDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.CertificateMapper;
import com.freelancemarketplace.backend.mapper.FreelancerMapper;
import com.freelancemarketplace.backend.model.CertificateModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.repository.CertificatesRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.service.CertificateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateServiceImp implements CertificateService {

    private final CertificatesRepository certificatesRepository;
    private final CertificateMapper certificateMapper;
    private final FreelancersRepository freelancersRepository;
    private final FreelancerMapper freelancerMapper;

    @Override
    @Transactional
    public CertificateDTO createCertificate(Long freelancerId, CertificateDTO certificateDTO) {

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );


        CertificateModel newCertificate = certificateMapper.toEntity(certificateDTO);
        newCertificate.setFreelancer(freelancer);
        CertificateModel savedCertificate = certificatesRepository.save(newCertificate);
        freelancer.getCertificates().add(newCertificate);
        FreelancerModel saveFreelancer = freelancersRepository.save(freelancer);
        return certificateMapper.toDto(savedCertificate);
    }

    @Override
    @Transactional
    public CertificateDTO updateCertificate(Long certificateId, CertificateDTO certificateDTO) {
        CertificateModel certificate = certificatesRepository.findById(certificateId).orElseThrow(
                ()-> new ResourceNotFoundException("Certification with id: " + certificateId + " not found")
        );
        CertificateModel updatedCertificate = certificateMapper.partialUpdate(certificateDTO, certificate);
        CertificateModel savedCertificate = certificatesRepository.save(updatedCertificate);
        FreelancerModel freelancer = savedCertificate.getFreelancer();
        return certificateMapper.toDto(savedCertificate);
    }

    @Override
    @Transactional
    public void deleteCertificate(Long certificateId) {
        CertificateModel certificate = certificatesRepository.findById(certificateId).orElseThrow(
                ()-> new ResourceNotFoundException("Certification with id: " + certificateId + " not found")
        );
        FreelancerModel freelancer = certificate.getFreelancer();
        freelancer.getCertificates().remove(certificate);
        certificatesRepository.delete(certificate);
    }

    @Override
    public List<CertificateDTO> getAllCertificateByFreelancer(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );
        List<CertificateModel> certificates = certificatesRepository.findAllByFreelancer(freelancer);
        return certificateMapper.toDTOs(certificates);
    }
}
