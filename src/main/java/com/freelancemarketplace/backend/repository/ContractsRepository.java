package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractsRepository extends JpaRepository<ContractModel, Long> {
  List<ContractModel> findAllByFreelancer(FreelancerModel freelancer);
  }