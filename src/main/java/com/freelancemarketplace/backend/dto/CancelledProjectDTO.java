package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CancelledProjectDTO {
    Long projectId;
    String projectName;
    String clientName;
}
