package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.LocationsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationsRepository extends JpaRepository<LocationsModel, Long> {
  }