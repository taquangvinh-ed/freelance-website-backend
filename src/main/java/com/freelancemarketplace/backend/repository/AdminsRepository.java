package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminsRepository extends JpaRepository<AdminModel, Long> {
    Boolean existsByPhoneNumber(String phoneNumber);
}