package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.FreelancerDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.FreelancerMapper;
import com.freelancemarketplace.backend.mapper.UserMapper;
import com.freelancemarketplace.backend.model.Bio;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.UserModel;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.UserRepository;
import com.freelancemarketplace.backend.service.FreelancerService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FreelancerServiceImp implements FreelancerService {

    private final FreelancersRepository freelancersRepository;
    private final FreelancerMapper freelancerMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;


    public FreelancerServiceImp(FreelancersRepository freelancersRepository, FreelancerMapper freelancerMapper, PasswordEncoder passwordEncoder, UserMapper userMapper,
                                UserRepository userRepository) {
        this.freelancersRepository = freelancersRepository;
        this.freelancerMapper = freelancerMapper;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public FreelancerDTO createFreelancer(FreelancerDTO freelancerDTO) {

        UserModel newUser = userMapper.toEntity(freelancerDTO.getUser());

        newUser.setPasswordHash(passwordEncoder.encode(freelancerDTO.getUser().getPassword()));

        UserModel savedUser = userRepository.save(newUser);

        FreelancerModel newFreelancer = freelancerMapper.toEntity(freelancerDTO);
        newFreelancer.setUser(newUser);
        newFreelancer.setFreelancerId(savedUser.getUserId());
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
public FreelancerDTO getFreelancerById(Long freelancerId){
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
    ()-> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found"));
        return freelancerMapper.toDTO(freelancer);

    }
}
