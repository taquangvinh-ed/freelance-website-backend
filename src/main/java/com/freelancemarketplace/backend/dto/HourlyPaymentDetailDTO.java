package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HourlyPaymentDetailDTO extends HourlyPaymentSummaryDTO {
    private Long clientId;
    private Long freelancerId;
    private String note;
}

