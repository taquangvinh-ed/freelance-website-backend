package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.CategoryModel;
import com.freelancemarketplace.backend.model.SkillModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillsRepository extends JpaRepository<SkillModel, Long>, JpaSpecificationExecutor<SkillModel> {
  Boolean existsByNameIgnoreCase(String skillName);
  List<SkillModel> findAllByCategoriesContains(CategoryModel categoryModel);
}