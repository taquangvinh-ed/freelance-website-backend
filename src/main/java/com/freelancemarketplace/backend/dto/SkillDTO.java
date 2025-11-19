package com.freelancemarketplace.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SkillDTO {
    private Long skillId;

    @NotEmpty(message = "Skill name must not be empty")
    private String name;

    private String description;

    private Set<CategoryResponse> categories = new HashSet<>();
}
