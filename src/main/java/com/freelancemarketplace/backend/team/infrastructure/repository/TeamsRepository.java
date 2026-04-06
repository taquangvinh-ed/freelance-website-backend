package com.freelancemarketplace.backend.team.infrastructure.repository;

import com.freelancemarketplace.backend.team.domain.model.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepository extends JpaRepository<TeamModel, Long> {
  }