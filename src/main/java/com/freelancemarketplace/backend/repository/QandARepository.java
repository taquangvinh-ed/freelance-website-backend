package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.QandAModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QandARepository extends JpaRepository<QandAModel, Long> {
  }