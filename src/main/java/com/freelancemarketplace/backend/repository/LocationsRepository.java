package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.LocationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationsRepository extends JpaRepository<LocationModel, Long> {
  }