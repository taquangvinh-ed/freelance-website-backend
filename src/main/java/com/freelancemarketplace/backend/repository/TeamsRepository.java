package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepository extends JpaRepository<TeamModel, Long> {
  }