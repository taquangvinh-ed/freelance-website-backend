package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.enums.TimeLogStatus;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    boolean existsByFreelancerAndStatus(FreelancerModel freelancer, TimeLogStatus status);

    // 2. Tìm bản ghi ACTIVE của một Freelancer (dùng cho hàm STOP)
    Optional<TimeLog> findByFreelancerAndStatus(FreelancerModel freelancer, TimeLogStatus status);
}