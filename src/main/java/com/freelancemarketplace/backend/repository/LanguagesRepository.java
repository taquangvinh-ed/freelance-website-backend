package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.LanguagesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguagesRepository extends JpaRepository<LanguagesModel, Long> {
  }