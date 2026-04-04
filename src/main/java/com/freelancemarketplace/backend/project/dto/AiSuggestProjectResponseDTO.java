package com.freelancemarketplace.backend.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiSuggestProjectResponseDTO {

    private AiSuggestedProjectDTO suggestedProject;
    private List<String> warnings = new ArrayList<>();
    private String requestId;
}

