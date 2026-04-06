package com.freelancemarketplace.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "LegacyWeeklyReportModel")
@Table(name = "weekly_reports")
public class WeeklyReportModel {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long weeklyReportId;

    @OneToMany(mappedBy = "weeklyReport")
    private List<WeeklyReportItemModel> items;

    double totalHours;

    @ManyToOne
    @JoinColumn(name = "contractId")
    private ContractModel contract;
}
