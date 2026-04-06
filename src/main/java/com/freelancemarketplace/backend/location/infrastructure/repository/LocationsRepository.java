package com.freelancemarketplace.backend.location.infrastructure.repository;

import com.freelancemarketplace.backend.location.domain.model.LocationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationsRepository extends JpaRepository<LocationModel, Long> {
  }