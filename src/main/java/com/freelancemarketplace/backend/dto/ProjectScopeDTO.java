package com.freelancemarketplace.backend.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectScopeDTO {
    private String duration; // "1 to 3 months"


    private String level; // "Intermediate"


    private String workload; // "Part-time", "Full-time" (tùy chọn)
}
