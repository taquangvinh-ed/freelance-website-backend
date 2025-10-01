package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.CategoryDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.CategoryMapper;
import com.freelancemarketplace.backend.model.CategoryModel;
import com.freelancemarketplace.backend.repository.CategoriesRepository;
import com.freelancemarketplace.backend.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoriesRepository categoriesRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImp(CategoriesRepository categoriesRepository, CategoryMapper categoryMapper) {
        this.categoriesRepository = categoriesRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        CategoryModel newCategory = categoryMapper.toEntity(categoryDTO);
        CategoryModel savedCategory = categoriesRepository.save(newCategory);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        CategoryModel category = categoriesRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category with id: " + categoryId + " not found")
        );

        CategoryModel updatedCategory = categoryMapper.partialUpdate(categoryDTO,category);
        CategoryModel savedCategory = categoriesRepository.save(updatedCategory);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if(!categoriesRepository.existsById(categoryId))
            throw new ResourceNotFoundException("Category with id: " + categoryId + " not found");
        categoriesRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        List<CategoryModel> categories = categoriesRepository.findAll();
        return categoryMapper.toDTOs(categories);
    }
}
