package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.AdminsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminsRepository extends JpaRepository<AdminsModel, Long> {
}