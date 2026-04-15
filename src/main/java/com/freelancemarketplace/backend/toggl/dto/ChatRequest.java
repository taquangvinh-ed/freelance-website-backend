package com.freelancemarketplace.backend.toggl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @NotBlank(message = "Message cannot be blank")
    @Size(min = 1, max = 1000, message = "Message must be between 1 and 1000 characters")
    private String message;

    private ProjectContext projectContext;

    private Long recommendationId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectContext {
        private String title;
        private String description;
        private Long categoryId;
        private String scope;
        private String timeline;
        private String experienceLevel;
        private String budgetMin;
        private String budgetMax;
        private java.util.List<String> skills;
    }
}
