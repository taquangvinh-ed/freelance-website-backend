package com.freelancemarketplace.backend.project.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import com.freelancemarketplace.backend.category.dto.CategoryDTO;
import com.freelancemarketplace.backend.freelancer.dto.AdvancedPreferencesDTO;
import com.freelancemarketplace.backend.project.dto.BudgetDTO;
import com.freelancemarketplace.backend.project.dto.ProjectScopeDTO;
import com.freelancemarketplace.backend.skill.dto.SkillDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {

    private String title;
    private String description;
    private CategoryDTO category;
    private Set<SkillDTO> skills = new HashSet<>();
    private BudgetDTO budget;
    private ProjectScopeDTO scope;
    private AdvancedPreferencesDTO advancedPreferences;

}
