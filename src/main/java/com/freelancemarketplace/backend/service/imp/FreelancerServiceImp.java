package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.FreelancerDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.FreelancerMapper;
import com.freelancemarketplace.backend.model.Bio;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.recommandation.EmbeddingService;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.SkillsRepository;
import com.freelancemarketplace.backend.repository.TestimonialsRepository;
import com.freelancemarketplace.backend.service.FreelancerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FreelancerServiceImp implements FreelancerService {

    private final FreelancersRepository freelancersRepository;
    private final FreelancerMapper freelancerMapper;
    private final SkillsRepository skillsRepository;
    private final EmbeddingService embeddingService;
    private final TestimonialsRepository testimonialsRepository;



    @Override
    @Transactional
    public FreelancerDTO createFreelancer(FreelancerDTO freelancerDTO) {

        FreelancerModel newFreelancer = freelancerMapper.toEntity(freelancerDTO);
        FreelancerModel savedFreelancer = freelancersRepository.save(newFreelancer);

        return freelancerMapper.toDTO(savedFreelancer);
    }

    @Override
    @Transactional
    public FreelancerDTO updateFreelancer(Long freelancerId, FreelancerDTO freelancerDTO) {

        FreelancerModel existingFreelancer = freelancersRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found with id: " + freelancerId));

        if (freelancerDTO.getFirstName() != null) {
            existingFreelancer.setFirstName(freelancerDTO.getFirstName());
        }
        if (freelancerDTO.getLastName() != null) {
            existingFreelancer.setLastName(freelancerDTO.getLastName());
        }
        if (freelancerDTO.getTitle() != null) {
            existingFreelancer.setTitle(freelancerDTO.getTitle());
        }


        if (freelancerDTO.getHourlyRate() != null) {
            existingFreelancer.setHourlyRate(freelancerDTO.getHourlyRate());
        }


        if (freelancerDTO.getBio() != null) {

            Bio updatedBio = freelancerDTO.getBio();
            Bio existingBio = existingFreelancer.getBio();

            if (existingBio == null) {
                existingFreelancer.setBio(updatedBio);
            } else {
                if (updatedBio.getDateOfBirth() != null) {
                    existingBio.setDateOfBirth(updatedBio.getDateOfBirth());
                }
                if (updatedBio.getGender() != null) {
                    existingBio.setGender(updatedBio.getGender());
                }
                if (updatedBio.getNationality() != null) {
                    existingBio.setNationality(updatedBio.getNationality());
                }
                if (updatedBio.getSummary() != null) {
                    existingBio.setSummary(updatedBio.getSummary());
                }
                if (updatedBio.getTwitterLink() != null) {
                    existingBio.setTwitterLink(updatedBio.getTwitterLink());
                }
                if (updatedBio.getLinkedinLink() != null) {
                    existingBio.setLinkedinLink(updatedBio.getLinkedinLink());
                }
                if (updatedBio.getFacebookLink() != null) {
                    existingBio.setFacebookLink(updatedBio.getFacebookLink());
                }
            }
        }

        // 4. Lưu entity đã cập nhật
        FreelancerModel savedFreelancer = freelancersRepository.save(existingFreelancer);
        return freelancerMapper.toDTO(savedFreelancer);
    }

    @Override
    @Transactional
    public void deleteFreelancer(Long freelancerId) {
        if (!freelancersRepository.existsById(freelancerId)) {
            throw new ResourceNotFoundException("Freelancer not found with id: " + freelancerId);
        }
        freelancersRepository.deleteById(freelancerId);
    }


    @Override
    @Transactional
    public List<FreelancerDTO> getAllFreelancer() {
        List<FreelancerModel> freelancers = freelancersRepository.findAll();

        return freelancerMapper.toDTOs(freelancers);
    }

    @Override
    @Transactional
    public FreelancerDTO getFreelancerById(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found"));
        FreelancerDTO freelancerDTO = freelancerMapper.toDTO(freelancer);

        long numberOfReviews = testimonialsRepository.countByFreelancer(freelancer);
        freelancerDTO.setReviews((int) numberOfReviews);

        double averageRating = testimonialsRepository.findAllByFreelancer(freelancer).stream().mapToDouble(testimonialModel -> testimonialModel.getRatingScore())
                .average().orElse(0.0);
        freelancerDTO.setAverageScore(averageRating);
        return freelancerDTO;
    }

    @Override
    public FreelancerModel findById(Long freelancerId){
        return freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );
    }


    @Override
    public FreelancerDTO assignSkillToFreelancer(Long freelancerId, Long skillId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                () -> new ResourceNotFoundException("Skill with id: " + skillId + " not found"));

        freelancer.getSkills().add(skill);
        freelancer.setSkillVector(embeddingService.generateSkillVector(freelancer));

        FreelancerModel savedFreelancer = freelancersRepository.save(freelancer);
        return freelancerMapper.toDTO(savedFreelancer);
    }

    @Override
    public FreelancerDTO removeSkillFromFreelancer(Long freelancerId, Long skillId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                () -> new ResourceNotFoundException("Skill with id: " + skillId + " not found"));

        freelancer.getSkills().remove(skill);
        freelancer.setSkillVector(embeddingService.generateSkillVector(freelancer));

        FreelancerModel savedFreelancer = freelancersRepository.save(freelancer);

        return freelancerMapper.toDTO(savedFreelancer);

    }


    @Override
    public void markOnboardingCompleted(String stripeAccountId){
        FreelancerModel freelancer = freelancersRepository.findByStripeAccountId(stripeAccountId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with stripeAccountId: " + stripeAccountId + " not found")
        );

        if(!freelancer.getOnboardingCompleted()){
            freelancer.setOnboardingCompleted(true);
            freelancersRepository.save(freelancer);
            log.info("Mark onboarding completed successfully");
        }
    }
}
