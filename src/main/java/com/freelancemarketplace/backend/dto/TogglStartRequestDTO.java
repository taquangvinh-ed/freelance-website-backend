package com.freelancemarketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TogglStartRequestDTO {
    private String userId;
    private String start; // Timestamp bắt đầu (ISO 8601 format)
    private String description;
    private Long project_id;
    private String created_with;
    private Long workspace_id;
    private Long duration;
    private boolean billable = true;
}
