package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class WeeklyReportItemDTO {
    private Long weeklyReportItemId;
    Instant startTime;
    Instant endTime;
    String description;
}
