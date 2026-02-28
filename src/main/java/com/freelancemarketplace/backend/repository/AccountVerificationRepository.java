package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.enums.VerificationStatus;
import com.freelancemarketplace.backend.model.AccountVerificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountVerificationRepository extends JpaRepository<AccountVerificationModel, Long> {

    Optional<AccountVerificationModel> findByUser_UserId(Long userId);

    List<AccountVerificationModel> findByStatus(VerificationStatus status);

    List<AccountVerificationModel> findByStatusOrderBySubmittedAtDesc(VerificationStatus status);
}

