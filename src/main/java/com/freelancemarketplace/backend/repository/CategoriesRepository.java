package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoryModel, Long> {
  }