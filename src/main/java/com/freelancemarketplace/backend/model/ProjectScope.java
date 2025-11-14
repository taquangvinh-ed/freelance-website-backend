package com.freelancemarketplace.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectScope {

    @Column(name = "scope_duration")
    private String duration; // "1 to 3 months"

    @Column(name = "scope_level")
    private String level; // "Intermediate"

    @Column(name = "scope_workload")
    private String workload; // "Part-time", "Full-time" (tùy chọn)
}
