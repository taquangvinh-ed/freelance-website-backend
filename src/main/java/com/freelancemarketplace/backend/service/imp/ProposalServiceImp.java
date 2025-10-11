package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ProjectProposalDTO;
import com.freelancemarketplace.backend.dto.ProposalDTO;
import com.freelancemarketplace.backend.enums.ProposalStatus;
import com.freelancemarketplace.backend.exception.ProposalException;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ProposalMapper;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.ProposalModel;
import com.freelancemarketplace.backend.model.TeamModel;
import com.freelancemarketplace.backend.repository.*;
import com.freelancemarketplace.backend.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProposalServiceImp implements ProposalService {

    private final ProposalsRepository proposalsRepository;
    private final ProposalMapper proposalMapper;
    private final FreelancersRepository freelancersRepository;
    private final TeamsRepository teamsRepository;
    private final ProjectsRepository projectsRepository;
    private final UserRepository userRepository;


    @Override
    public ProposalDTO createProposal(Long freelancerId, ProposalDTO proposalDTO) {

        ProposalModel newProposal = proposalMapper.toEntity(proposalDTO);

            FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                    ()->new ResourceNotFoundException("Freelancer id: " + freelancerId + "inside proposal creating request create not found")
            );
            newProposal.setFreelancer(freelancer);


        if(proposalDTO.getTeamId() != null){
            TeamModel team = teamsRepository.findById(proposalDTO.getTeamId()).orElseThrow(
                    ()->new ResourceNotFoundException("Team id: " + proposalDTO.getTeamId() + "inside proposal creating request create not found")
            );
            newProposal.setTeam(team);
        }
            ProjectModel project = projectsRepository.findById(proposalDTO.getProjectId()).orElseThrow(
                    ()->new ResourceNotFoundException("Project id: " + proposalDTO.getProjectId() + "inside proposal creating request create not found")
            );

            if(proposalDTO.getAmount().compareTo(project.getBudget().getMaxValue()) > 0)
                throw new ProposalException("The proposal price exceeds the maximum budget.");
            newProposal.setProject(project);

            ProposalModel savedProposal = proposalsRepository.save(newProposal);


        return proposalMapper.toDto(savedProposal);
    }

    @Override
    public ProposalDTO updateProposal(Long proposalId, ProposalDTO proposalDTO) {

        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                ()->new ResourceNotFoundException("Proposal with id: " + proposalId + " not found")
        );

        ProposalModel updatedProposal = proposalMapper.partialUpdate(proposalDTO,proposal);

        ProposalModel savedProposal = proposalsRepository.save(updatedProposal);

        return proposalMapper.toDto(savedProposal);
    }

    @Override
    public void deleteProposal(Long proposalId) {
        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                ()->new ResourceNotFoundException("Proposal with id: " + proposalId + " not found")
        );

        proposalsRepository.deleteById(proposalId);
    }

    @Override
    public List<ProposalDTO> getAllProposalByFreelancerId(Long freelancerId){
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()->new ResourceNotFoundException("Freelancer id: " + freelancerId + "not found")
        );

        List<ProposalModel> proposals = proposalsRepository.findAllByFreelancer(freelancer);
        return proposalMapper.toDTOs(proposals);
    }

    @Override
    public List<ProposalDTO> getAllProposalByTeamId(Long teamId){
        TeamModel team = teamsRepository.findById(teamId).orElseThrow(
                ()->new ResourceNotFoundException("team id: " + teamId + "not found")
        );

        List<ProposalModel> proposals = proposalsRepository.getAllByTeam(team);
        return proposalMapper.toDTOs(proposals);
    }


    @Override
    public Page<ProjectProposalDTO> getAllProposalByProject(Long projectId, Pageable pageable){
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                ()-> new ResourceNotFoundException("Project with id: " + projectId + " not found")
        );

        List<ProposalModel> proposals = proposalsRepository.findAllByProject(project);

       List<ProjectProposalDTO> projectProposals =  proposals.stream().map((proposal) -> {
            FreelancerModel freelancer = proposal.getFreelancer();
            ProjectProposalDTO projectProposal = new ProjectProposalDTO();
            projectProposal.setId(proposal.getProposalId());
            projectProposal.setImageUrl(freelancer.getAvatar());
            projectProposal.setFirstName(freelancer.getFirstName());
            projectProposal.setLastName(freelancer.getLastName());
//            projectProposal.setUsername(freelancer.getUser().getUsername())pr;
            projectProposal.setProposalDescription(proposal.getDescription());
            projectProposal.setAmount(proposal.getAmount());
            projectProposal.setDeliveryDays(projectProposal.getDeliveryDays());
            return projectProposal;
        }).collect(Collectors.toList());

        return new PageImpl<>(projectProposals, pageable, projectProposals.size());
    }

    @Override
    public void acceptProposal(Long proposalId){
        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                ()->new ResourceNotFoundException("Proposal id: " + proposalId + "not found")
        );
        if (proposal.getStatus().equals(ProposalStatus.PENDING))
            proposal.setStatus(ProposalStatus.ACCEPTED);
        else
            throw new ProposalException("Cannot proposal cannot be accepted from status " + proposal.getStatus());
    }

    @Override
    public void rejectProposal(Long proposalId){
        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                ()->new ResourceNotFoundException("Proposal id: " + proposalId + "not found")
        );
        if (proposal.getStatus().equals(ProposalStatus.PENDING))
            proposal.setStatus(ProposalStatus.REJECTED);
        else
            throw new ProposalException("Cannot proposal cannot be rejected from status " + proposal.getStatus());
    }

    @Override
    public void withdrawProposal(Long proposalId){
        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                ()->new ResourceNotFoundException("Proposal id: " + proposalId + "not found")
        );
        if (proposal.getStatus().equals(ProposalStatus.PENDING))
            proposal.setStatus(ProposalStatus.WITHDRAWN);
        else
            throw new ProposalException(" Proposal cannot be withdraw from status " + proposal.getStatus());
    }





}
