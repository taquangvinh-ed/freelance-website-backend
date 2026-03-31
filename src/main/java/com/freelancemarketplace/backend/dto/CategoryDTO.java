package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryId;

    private String name;

    private String image;

    private Set<SkillDTO> skills = new HashSet<>();
}
