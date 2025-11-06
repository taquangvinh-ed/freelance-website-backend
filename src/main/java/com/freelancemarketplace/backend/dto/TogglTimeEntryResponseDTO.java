package com.freelancemarketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TogglTimeEntryResponseDTO {
    private String id;
    private String description;
    private String project_id;
    private Long duration;
    private String start;
    private String stop;

}
