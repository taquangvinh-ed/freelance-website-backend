package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.TeamsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepository extends JpaRepository<TeamsModel, Long> {
  }