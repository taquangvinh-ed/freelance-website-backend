package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
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
