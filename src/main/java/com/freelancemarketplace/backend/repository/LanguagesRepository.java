package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.LanguageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguagesRepository extends JpaRepository<LanguageModel, Long> {
  Boolean existsByLanguageName(String languageName);
  }