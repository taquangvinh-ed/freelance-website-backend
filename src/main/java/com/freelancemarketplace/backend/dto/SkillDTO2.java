package com.freelancemarketplace.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SkillDTO2 {
    private Long skillId;

    @NotEmpty(message = "Skill name must not be empty")
    private String name;

    private String description;

    private List<Long> categoryIds;
}
