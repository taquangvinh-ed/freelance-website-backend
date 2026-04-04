package com.freelancemarketplace.backend.admin.infrastructure.repository;

import com.freelancemarketplace.backend.admin.domain.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminsRepository extends JpaRepository<AdminModel, Long> {
    Boolean existsByPhoneNumber(String phoneNumber);
}