package com.freelancemarketplace.backend.location.infrastructure.repository;

import com.freelancemarketplace.backend.location.domain.model.CityModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitiesRepository extends JpaRepository<CityModel, Long> {
}
