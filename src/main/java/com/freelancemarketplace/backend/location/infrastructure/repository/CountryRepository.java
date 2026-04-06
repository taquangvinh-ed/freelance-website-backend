package com.freelancemarketplace.backend.location.infrastructure.repository;

import com.freelancemarketplace.backend.location.domain.model.CountryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryModel,Long > {
    Optional<CountryModel> findByCountryName(String countryName);
    Boolean existsByCountryName(String countryName);
}
