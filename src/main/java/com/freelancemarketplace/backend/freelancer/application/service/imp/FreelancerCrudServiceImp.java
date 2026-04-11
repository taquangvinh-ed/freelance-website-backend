package com.freelancemarketplace.backend.freelancer.application.service.imp;

import com.freelancemarketplace.backend.freelancer.application.service.FreelancerCrudService;
import com.freelancemarketplace.backend.freelancer.domain.model.Bio;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.dto.FreelancerDTO;
import com.freelancemarketplace.backend.freelancer.infrastructure.mapper.FreelancerMapper;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.review.infrastructure.repository.TestimonialsRepository;
import com.freelancemarketplace.backend.exceptionHandling.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreelancerCrudServiceImp implements FreelancerCrudService {

    private final FreelancersRepository freelancersRepository;
    private final FreelancerMapper freelancerMapper;
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

        double averageRating = testimonialsRepository.findAllByFreelancer(freelancer).stream()
                .mapToDouble(testimonialModel -> testimonialModel.getRatingScore())
                .average().orElse(0.0);
        freelancerDTO.setAverageScore(averageRating);
        return freelancerDTO;
    }
}
