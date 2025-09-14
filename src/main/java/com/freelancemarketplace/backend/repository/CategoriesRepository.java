package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.CategoriesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesModel, Long> {
  }