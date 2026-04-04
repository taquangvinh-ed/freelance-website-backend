package com.freelancemarketplace.backend.category.api.controller;

import com.freelancemarketplace.backend.project.dto.BudgetDTO;
import com.freelancemarketplace.backend.category.dto.CategoryDTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.category.application.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/api/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/new-category")
    public ApiResponse<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO newCategory = categoryService.createCategory(categoryDTO);
        return ApiResponse.created(newCategory);
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<?> updateCategory(@PathVariable Long categoryId,
                                                    @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        return ApiResponse.success(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.noContent();
    }

    @GetMapping("/getAll")
    public ApiResponse<?> getAllCategories() {
        List<CategoryDTO> categoryList = categoryService.getAllCategory();
            return ApiResponse.success(categoryList);
    }

}
