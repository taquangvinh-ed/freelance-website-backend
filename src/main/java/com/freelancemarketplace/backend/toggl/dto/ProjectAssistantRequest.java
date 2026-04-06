package com.freelancemarketplace.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Request DTO for AI Project Assistant
 * Client sends their project brief and preferences
 */
@Getter
@Setter
@NoArgsConstructor
public class ProjectAssistantRequest {

    @NotBlank(message = "Brief cannot be blank")
    @Size(min = 10, max = 2000, message = "Brief must be between 10 and 2000 characters")
    private String brief;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    @NotNull(message = "Scope cannot be null")
    private String scope; // SMALL, MEDIUM, LARGE, ENTERPRISE

    @NotNull(message = "Timeline cannot be null")
    private String timeline; // Less than 1 month, 1 to 3 months, 3 to 6 months, More than 6 months

    private List<String> preferredSkills; // Optional: skills client already knows they need

    private String experienceLevel; // BEGINNER, INTERMEDIATE, EXPERT (defaults to INTERMEDIATE)

    private String region; // Optional: for regional pricing adjustment

    private String complexityHint; // Optional: LOW, MEDIUM, HIGH (user's own assessment)
}

