package com.freelancemarketplace.backend.recommandation.imp;

import com.freelancemarketplace.backend.enums.InteractionType;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.recommandation.InteractionService;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProjectInteractionModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.ProjectInteractionModelRepository;
import com.freelancemarketplace.backend.repository.ProjectsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@AllArgsConstructor
public class InteractionServiceImp implements InteractionService {

    private final FreelancersRepository freelancersRepository;
    private final ProjectsRepository projectsRepository;
    private final ProjectInteractionModelRepository projectInteractionModelRepository;

    @Override
    public void logInteraction(Long freelancerId, Long projectId, InteractionType interactionType) {
        ProjectInteractionModel newInteraction = new ProjectInteractionModel();

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer with id: "+ freelancerId+ " not found")
        );

        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("Project with id: " + projectId +" not found")
        );

        newInteraction.setFreelancer(freelancer);
        newInteraction.setProject(project);
        newInteraction.setInteractionType(interactionType);
        newInteraction.setTimestamp(Timestamp.from(Instant.now()));
        newInteraction.setPositive(interactionType == InteractionType.WON);
        projectInteractionModelRepository.save(newInteraction);
    }
}
