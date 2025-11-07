package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.TimeLogStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "time_log")
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeLogId;

    @Column(name = "toggl_entry_id", nullable = false, unique = true)
    private String togglEntryId;

    @Enumerated(EnumType.STRING)
    private TimeLogStatus status;

    @Column(name = "start_time", nullable = false)
    private Instant startTime; // Dùng Instant cho thời gian chính xác (ISO 8601)

    @Column(length = 1000)
    private String description;

    @Column(name = "end_time")
    private Instant endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId")
    private ContractModel contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

}