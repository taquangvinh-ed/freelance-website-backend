package com.freelancemarketplace.backend.skill.infrastructure.repository;

import com.freelancemarketplace.backend.category.domain.model.CategoryModel;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillsRepository extends JpaRepository<SkillModel, Long>, JpaSpecificationExecutor<SkillModel> {
  Boolean existsByNameIgnoreCase(String skillName);
  List<SkillModel> findAllByCategoriesContains(CategoryModel categoryModel);

  Optional<SkillModel> findByNameIgnoreCase(String skillName);
}