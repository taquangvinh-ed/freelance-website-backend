package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.CountryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryModel,Long > {
    Optional<CountryModel> findByCountryName(String countryName);
    Boolean existsByCountryName(String countryName);
}
