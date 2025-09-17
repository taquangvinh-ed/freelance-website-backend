package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.Q_AModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Q_ARepository extends JpaRepository<Q_AModel, Long> {
  }