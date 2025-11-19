package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MileStoneDTO {
    private Long mileStoneId;

    private String name;

    private BigDecimal amount;

    private String currencyUnit;

    private Integer dueDate;

    private String description;

    private String freelancerId;

    private Boolean isActivated;

    private String status;

    private Long contractId;

    private Long proposalId;

    private String fileUrl;
    private String fileName;

    private String clientSecret;
    private Timestamp completedAt;

}
