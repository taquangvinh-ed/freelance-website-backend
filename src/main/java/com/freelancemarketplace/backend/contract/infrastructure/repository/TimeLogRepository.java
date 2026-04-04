package com.freelancemarketplace.backend.contract.infrastructure.repository;

import com.freelancemarketplace.backend.contract.domain.enums.TimeLogStatus;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.contract.domain.model.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    boolean existsByFreelancerAndStatus(FreelancerModel freelancer, TimeLogStatus status);

    // 2. Tìm bản ghi ACTIVE của một Freelancer (dùng cho hàm STOP)
    List<TimeLog> findByFreelancerAndStatus(FreelancerModel freelancer, TimeLogStatus status);

    List<TimeLog> findByContractAndStatusAndEndTimeBetween(ContractModel contract, TimeLogStatus status, Instant startTime, Instant endTime);

    @Query("""
            SELECT t FROM TimeLog t
            WHERE t.contract = :contract
              AND t.status = :status
              AND t.startTime < :periodEnd
              AND t.endTime IS NOT NULL
              AND t.endTime > :periodStart
            """)
    List<TimeLog> findOverlappingLogs(
            @Param("contract") ContractModel contract,
            @Param("status") TimeLogStatus status,
            @Param("periodStart") Instant periodStart,
            @Param("periodEnd") Instant periodEnd
    );
}