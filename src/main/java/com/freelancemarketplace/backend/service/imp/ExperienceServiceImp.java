package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ExperienceDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ExperienceMapper;
import com.freelancemarketplace.backend.model.ExperienceModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.repository.ExperiencesRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImp implements ExperienceService {

    private final ExperiencesRepository experiencesRepository;
    private final ExperienceMapper experienceMapper;
    private final FreelancersRepository freelancersRepository;


    @Override
    public ExperienceDTO create(Long freelancerId, ExperienceDTO experienceDTO) {


        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer not found")
        );

        ExperienceModel newExperience = experienceMapper.toEntity(experienceDTO);
        newExperience.setFreelancer(freelancer);
        ExperienceModel savedExperience = experiencesRepository.save(newExperience);

        return experienceMapper.toDto(savedExperience);
    }

    @Override
    public ExperienceDTO update(Long experienceId, ExperienceDTO experienceDTO) {

        ExperienceModel experience = experiencesRepository.findById(experienceId).orElseThrow(
                ()-> new ResourceNotFoundException("Experience not found with id: " + experienceId)
        );

        ExperienceModel updatedExperience = experienceMapper.partialUpdate(experienceDTO, experience);
        ExperienceModel savedExperience = experiencesRepository.save(updatedExperience);

        return experienceMapper.toDto(savedExperience);
    }

    @Override
    public void delete(Long experienceId) {
        ExperienceModel experience = experiencesRepository.findById(experienceId).orElseThrow(
                ()-> new ResourceNotFoundException("Experience not found with id: " + experienceId)
        );
        experiencesRepository.delete(experience);
    }

    @Override
    public List<ExperienceDTO> getAllExperienceByFreelancer(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer k                       ")
        );
        List<ExperienceModel> experienceList = experiencesRepository.findAllByFreelancer(freelancer);
        return experienceMapper.toDtos(experienceList);
    }
}
