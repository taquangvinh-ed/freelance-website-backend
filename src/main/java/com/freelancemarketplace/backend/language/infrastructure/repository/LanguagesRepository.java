package com.freelancemarketplace.backend.language.infrastructure.repository;

import com.freelancemarketplace.backend.language.domain.model.LanguageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguagesRepository extends JpaRepository<LanguageModel, Long> {
  Boolean existsByLanguageName(String languageName);
  Boolean existsByIsoCode(String isoCode);
  }