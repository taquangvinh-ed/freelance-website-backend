package com.freelancemarketplace.backend.contract.infrastructure.repository;

import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractsRepository extends JpaRepository<ContractModel, Long> {
  List<ContractModel> findAllByFreelancer(FreelancerModel freelancer);
  List<ContractModel> findAllByClient(ClientModel client);
  @EntityGraph(attributePaths = {"client", "client.user"})
  @Query("SELECT c FROM ContractModel c WHERE c.contractId = :contractId")
  Optional<ContractModel> findByIdWithClientAndUser(@Param("contractId") Long contractId);
}