package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ProposalModal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalsRepository extends JpaRepository<ProposalModal, Long> {
  }