package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.MileStoneStatus;
import com.freelancemarketplace.backend.model.ContractModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class MileStoneDTO {
    private Long mileStoneId;

    private String name;

    private BigDecimal amount;

    private Timestamp dueDate;

    private String description;

    private Boolean isActived;

    private String status;

    private Long contractId;
}
