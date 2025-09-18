package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ContractDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ContractMapper;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.repository.*;
import com.freelancemarketplace.backend.service.ContractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImp implements ContractService {


    private ContractsRepository contractsRepository;
    private ContractMapper contractMapper;
    private FreelancersRepository freelancersRepository;
    private TeamsRepository teamsRepository;
    private PaymentsRepository paymentsRepository;
    private ProjectsRepository projectsRepository;
    private final ProposalsRepository proposalsRepository;
    private final ClientsRepository clientsRepository;
    private final CompaniesRepository companiesRepository;

    public ContractServiceImp(ContractsRepository contractsRepository, ContractMapper contractMapper, FreelancersRepository freelancersRepository, TeamsRepository teamsRepository, PaymentsRepository paymentsRepository, ProjectsRepository projectsRepository,
                              ProposalsRepository proposalsRepository,
                              ClientsRepository clientsRepository,
                              CompaniesRepository companiesRepository) {
        this.contractsRepository = contractsRepository;
        this.contractMapper = contractMapper;
        this.freelancersRepository = freelancersRepository;
        this.teamsRepository = teamsRepository;
        this.paymentsRepository = paymentsRepository;
        this.projectsRepository = projectsRepository;
        this.proposalsRepository = proposalsRepository;
        this.clientsRepository = clientsRepository;
        this.companiesRepository = companiesRepository;
    }

    @Override
    public ContractDTO createContract(ContractDTO contractDTO) {

        ContractModel newContract = contractMapper.toEntity(contractDTO);

        if (contractDTO.getProposalId() != null) {
            ProposalModel proposal = proposalsRepository.findById(contractDTO.getProposalId()).orElseThrow(
                    () -> new ResourceNotFoundException("Proposal with id: " + contractDTO.getProposalId() + " not found")
            );
            newContract.setProposal(proposal);
        }


        // Validate Freelancer ID
        if (contractDTO.getFreelancerId() != null) {
            FreelancerModel freelancer = freelancersRepository.findById(contractDTO.getFreelancerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Freelancer with id: " + contractDTO.getFreelancerId() + " not found"));
            newContract.setFreelancer(freelancer);
        }

        // Validate Team ID
        if (contractDTO.getTeamId() != null) {
            TeamModel team = teamsRepository.findById(contractDTO.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team with id: " + contractDTO.getTeamId() + " not found"));

            newContract.setTeam(team);
        }

        // Validate Company ID
        if (contractDTO.getCompanyId() != null) {
            CompanyModel company = companiesRepository.findById(contractDTO.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company with id: " + contractDTO.getCompanyId() + " not found"));

            newContract.setCompany(company);

        }

        // Validate Client ID
        if (contractDTO.getClientId() != null) {
            ClientModel client = clientsRepository.findById(contractDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client with id: " + contractDTO.getClientId() + " not found"));
            newContract.setClient(client);
        }

        // Validate Project ID
        if (contractDTO.getContractProjectId() != null) {
            ProjectModel project = projectsRepository.findById(contractDTO.getContractProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project with id: " + contractDTO.getContractProjectId() + " not found"));
            newContract.setContractProject(project);

        }

        ContractModel savedContract = contractsRepository.save(newContract);

        return contractMapper.toDto(savedContract);
    }

    @Override
    public ContractDTO updateContract(Long contractId, ContractDTO contractDTO) {
        return null;
    }

    @Override
    public void deleteContract(Long contractId) {

    }

    @Override
    public List<ContractDTO> findAllContractByFreelancerId(Long freelancerId) {
        return List.of();
    }
}
