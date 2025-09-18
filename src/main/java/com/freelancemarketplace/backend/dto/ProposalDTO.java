package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.ProposalStatus;
import com.freelancemarketplace.backend.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProposalDTO {

    private Long proposalId;

    private String name;

    private String description;

    private String[] files;

    private String status;

    private BigDecimal amount;

    private Integer deliveryDays;

    private Long freelancerId;

    @NotNull(message = "Project id must not be null")
    private Long projectId;

    private Long teamId;
}
