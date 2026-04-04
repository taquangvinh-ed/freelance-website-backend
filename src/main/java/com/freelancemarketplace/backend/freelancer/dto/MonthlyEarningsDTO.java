package com.freelancemarketplace.backend.freelancer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyEarningsDTO {
    String monthLabel;
    double earnings;
}
