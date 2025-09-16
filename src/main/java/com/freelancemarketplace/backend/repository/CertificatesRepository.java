package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.CertificateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificatesRepository extends JpaRepository<CertificateModel, Long> {
  }