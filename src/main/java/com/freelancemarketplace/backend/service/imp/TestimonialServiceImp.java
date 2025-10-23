package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.TestimonialDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.TestimonialMapper;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.repository.*;
import com.freelancemarketplace.backend.service.TestomonialService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class TestimonialServiceImp implements TestomonialService {

    private final TestimonialsRepository testimonialsRepository;
    private final TestimonialMapper testimonialMapper;
    private final FreelancersRepository freelancersRepository;
    private  final TeamsRepository teamsRepository;
    private final ClientsRepository clientsRepository;
    private final CompaniesRepository companiesRepository;
    private final ProjectsRepository projectsRepository;

    public TestimonialServiceImp(TestimonialsRepository testimonialsRepository, TestimonialMapper testimonialMapper, FreelancersRepository freelancersRepository, TeamsRepository teamsRepository, ClientsRepository clientsRepository, CompaniesRepository companiesRepository, ProjectsRepository projectsRepository) {
        this.testimonialsRepository = testimonialsRepository;
        this.testimonialMapper = testimonialMapper;
        this.freelancersRepository = freelancersRepository;
        this.teamsRepository = teamsRepository;
        this.clientsRepository = clientsRepository;
        this.companiesRepository = companiesRepository;
        this.projectsRepository = projectsRepository;
    }

    @Override
    public TestimonialDTO createReview(TestimonialDTO testimonialDTO) {
        TestimonialModel newReview = testimonialMapper.toEntity(testimonialDTO);
        newReview.setDate(Timestamp.from(Instant.now()));

        if(testimonialDTO.getFreelancerId() != null){
            FreelancerModel freelancer = freelancersRepository.findById(testimonialDTO.getFreelancerId()).orElseThrow(
                    ()->  new ResourceNotFoundException("Freelancer with id: " + testimonialDTO.getFreelancerId() + " not found"));
            newReview.setFreelancer(freelancer);
        }

        if(testimonialDTO.getTeamId() != null){
            TeamModel team = teamsRepository.findById(testimonialDTO.getTeamId()).orElseThrow(
                    ()->  new ResourceNotFoundException("Team with id: " + testimonialDTO.getTeamId() + " not found"));
            newReview.setTeam(team);
        }

        if(testimonialDTO.getClientId() != null){
            ClientModel client = clientsRepository.findById(testimonialDTO.getClientId()).orElseThrow(
                    ()->  new ResourceNotFoundException("Client with id: " + testimonialDTO.getClientId() + " not found"));
            newReview.setClient(client);
        }


        if(testimonialDTO.getProjectId() != null){
            ProjectModel project = projectsRepository.findById(testimonialDTO.getProjectId()).orElseThrow(
                    ()->  new ResourceNotFoundException("Project with id: " + testimonialDTO.getProjectId() + " not found"));
            newReview.setProject(project);
        }

        TestimonialModel savedReview = testimonialsRepository.save(newReview);

        return testimonialMapper.toDto(savedReview);
    }

    @Override
    public TestimonialDTO updateReview(Long testimonialId, TestimonialDTO testimonialDTO) {
        TestimonialModel testimonial = testimonialsRepository.findById(testimonialId).orElseThrow(
                ()-> new ResourceNotFoundException("Review with id: " + testimonialId + " not found")
        );

        TestimonialModel updatedReview = testimonialMapper.partialUpdate(testimonialDTO, testimonial);
        TestimonialModel savedReview = testimonialsRepository.save(updatedReview);

        return testimonialMapper.toDto(savedReview);
    }

    @Override
    public void deleteReview(Long testimonialId) {
        if (!testimonialsRepository.existsById(testimonialId))
            throw  new ResourceNotFoundException("Review with id: " + testimonialId + " not found");

        testimonialsRepository.deleteById(testimonialId);
    }

    @Override
    public Page<TestimonialDTO> getAllReviewByFreelancer(Long freelancerId, Pageable pageable) {

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()->  new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found"));

        Page<TestimonialModel> testimonialModelPage = testimonialsRepository.findAllByFreelancer(freelancer, pageable);
        return testimonialMapper.toDTOPage(testimonialModelPage, pageable);
    }

    @Override
    public Page<TestimonialDTO> getAllReviewByTeam(Long teamId, Pageable pageable) {
        TeamModel team = teamsRepository.findById(teamId).orElseThrow(
                ()->  new ResourceNotFoundException("Team with id: " + teamId + " not found"));
        Page<TestimonialModel> testimonialModelPage = testimonialsRepository.findAllByTeam(team, pageable);
        return testimonialMapper.toDTOPage(testimonialModelPage, pageable);
    }

    @Override
    public Page<TestimonialDTO> getAllReviewByClient(Long clientId, Pageable pageable) {

        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                ()->  new ResourceNotFoundException("Client with id: " + clientId + " not found"));
        Page<TestimonialModel> testimonialModelPage = testimonialsRepository.findAllByClient(client, pageable);

        return testimonialMapper.toDTOPage(testimonialModelPage, pageable);
    }

    @Override
    public Page<TestimonialDTO> getAllReviewByProject(Long projectId, Pageable pageable) {

        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                ()->  new ResourceNotFoundException("Project with id: " + projectId + " not found"));
        Page<TestimonialModel> testimonialModelPage = testimonialsRepository.findAllByProject(project, pageable);

        return testimonialMapper.toDTOPage(testimonialModelPage, pageable);
    }

    @Override
    public Page<TestimonialDTO> getAllReviewByCompany(Long companyId, Pageable pageable) {

        CompanyModel company = companiesRepository.findById(companyId).orElseThrow(
                ()->  new ResourceNotFoundException("Company with id: " + companyId + " not found"));
        Page<TestimonialModel> testimonialModelPage = testimonialsRepository.findAllByCompany(company, pageable);
        return testimonialMapper.toDTOPage(testimonialModelPage, pageable);
    }
}
