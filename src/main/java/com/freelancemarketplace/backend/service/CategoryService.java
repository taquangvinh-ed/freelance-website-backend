package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

    void deleteCategory(Long categoryId);

    List<CategoryDTO> getAllCategory();

}
