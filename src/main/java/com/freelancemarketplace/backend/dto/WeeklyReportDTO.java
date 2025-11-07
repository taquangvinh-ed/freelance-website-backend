package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WeeklyReportDTO {
    private Long weeklyReportId;

    private List<WeeklyReportItemDTO> items;

    double totalHours;
}
