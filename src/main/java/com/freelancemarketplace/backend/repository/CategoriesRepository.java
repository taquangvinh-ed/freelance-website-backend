package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.domain.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoryModel, Long> {
    Optional<CategoryModel> findByNameIgnoreCase(String name);
}
