package com.freelancemarketplace.backend.request;

import com.freelancemarketplace.backend.dto.BudgetDTO;
import com.freelancemarketplace.backend.dto.CategoryDTO;
import com.freelancemarketplace.backend.dto.ProjectScopeDTO;
import com.freelancemarketplace.backend.dto.SkillDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

}
