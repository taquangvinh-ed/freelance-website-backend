package com.freelancemarketplace.backend.freelancer.infrastructure.repository;

import com.freelancemarketplace.backend.certification.domain.model.CertificateModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificatesRepository extends JpaRepository<CertificateModel, Long> {
  List<CertificateModel>findAllByFreelancer(FreelancerModel freelancer);
  }