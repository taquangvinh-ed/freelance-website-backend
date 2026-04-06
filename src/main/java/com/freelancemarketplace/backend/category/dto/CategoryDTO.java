package com.freelancemarketplace.backend.category.dto;

import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import com.freelancemarketplace.backend.skill.dto.SkillDTO;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryId;

    private String name;

    private String image;

    private Set<SkillDTO> skills = new HashSet<>();
}
