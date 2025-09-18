package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProposalModel;
import com.freelancemarketplace.backend.model.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalsRepository extends JpaRepository<ProposalModel, Long> {
    List<ProposalModel> getAllByFreelancer(FreelancerModel freelancerModel);

    List<ProposalModel> getAllByTeam(TeamModel teamModel);
  }