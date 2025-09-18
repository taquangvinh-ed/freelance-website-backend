package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.MileStoneModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MileStoneModelRepository extends JpaRepository<MileStoneModel, Long> {
    List<MileStoneModel> findAllByContract(ContractModel contract);
}