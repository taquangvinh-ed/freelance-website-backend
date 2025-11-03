package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.CertificateModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificatesRepository extends JpaRepository<CertificateModel, Long> {
  List<CertificateModel>findAllByFreelancer(FreelancerModel freelancer);
  }