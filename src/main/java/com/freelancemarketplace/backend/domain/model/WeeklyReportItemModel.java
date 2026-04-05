package com.freelancemarketplace.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "LegacyWeeklyReportItemModel")
@Table(name = "weekly_report_item")
public class WeeklyReportItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weeklyReportItemId;
    Instant startTime;
    Instant endTime;
    String description;

    @ManyToOne
    @JoinColumn(name = "weeklyReportId")
    private WeeklyReportModel weeklyReport;
}
