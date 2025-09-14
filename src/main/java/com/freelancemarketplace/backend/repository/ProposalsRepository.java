package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ProposalsModal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalsRepository extends JpaRepository<ProposalsModal, Long> {
  }