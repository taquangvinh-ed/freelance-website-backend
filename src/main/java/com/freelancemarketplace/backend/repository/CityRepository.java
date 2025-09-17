package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.CityModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<CityModel, Long> {
    Optional<CityModel> findByCityName(String cityName);
    Boolean existsByCityName(String cityName);
}
