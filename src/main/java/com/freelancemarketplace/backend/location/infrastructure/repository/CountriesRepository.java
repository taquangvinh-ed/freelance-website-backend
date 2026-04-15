package com.freelancemarketplace.backend.location.infrastructure.repository;

import com.freelancemarketplace.backend.location.domain.model.CountryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountriesRepository extends JpaRepository<CountryModel, Long> {
}
