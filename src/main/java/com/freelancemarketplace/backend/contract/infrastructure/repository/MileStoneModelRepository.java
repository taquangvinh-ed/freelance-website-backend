package com.freelancemarketplace.backend.contract.infrastructure.repository;

import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.contract.domain.model.MileStoneModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MileStoneModelRepository extends JpaRepository<MileStoneModel, Long> {
    List<MileStoneModel> findAllByContract(ContractModel contract);
}