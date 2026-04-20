package com.freelancemarketplace.backend.proposal.infrastructure.repository;

import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.proposal.domain.model.ProposalModel;
import com.freelancemarketplace.backend.team.domain.model.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProposalsRepository extends JpaRepository<ProposalModel, Long> {
    List<ProposalModel> findAllByFreelancer(FreelancerModel freelancerModel);

    List<ProposalModel> getAllByTeam(TeamModel teamModel);

    @Query("SELECT p FROM ProposalModel p WHERE p.project = :project")
    @Transactional(readOnly = true)
    List<ProposalModel> findAllByProject(@Param("project") ProjectModel project);

    ProposalModel findByFreelancerAndProject(FreelancerModel freelancer, ProjectModel project);

    @Modifying
    @Query("UPDATE ProposalModel p SET p.status = 'REJECTED' " +
            "WHERE p.project.projectId = :projectId AND p.proposalId != :approvedProposalId " +
            "AND p.status = 'PENDING'")
    void rejectOtherProposalsInProject(@Param("projectId") Long projectId, @Param("approvedProposalId") Long approvedProposalId);
  }