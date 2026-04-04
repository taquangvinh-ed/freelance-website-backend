package com.freelancemarketplace.backend.category.application.service;

import com.freelancemarketplace.backend.category.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

    void deleteCategory(Long categoryId);

    List<CategoryDTO> getAllCategory();

}
