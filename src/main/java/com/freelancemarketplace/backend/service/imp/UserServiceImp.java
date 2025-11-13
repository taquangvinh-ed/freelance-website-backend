package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.RegistrationtDTO;
import com.freelancemarketplace.backend.enums.UserRoles;
import com.freelancemarketplace.backend.mapper.UserMapper;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.UserModel;
import com.freelancemarketplace.backend.repository.AdminsRepository;
import com.freelancemarketplace.backend.repository.ClientsRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.UserRepository;
import com.freelancemarketplace.backend.service.PaymentService;
import com.freelancemarketplace.backend.service.UserService;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FreelancersRepository freelancersRepository;
    private final ClientsRepository clientsRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;
    private final AdminsRepository adminsRepository;


    @Override
    @Transactional
    public RegistrationtDTO registerUser(RegistrationtDTO registrationtDTO) throws StripeException {
        RegistrationtDTO registrationtResponseDTO = new RegistrationtDTO();
        UserModel newUser = userMapper.registraionDtoToUserEntity(registrationtDTO);
        String hashPwd = passwordEncoder.encode(registrationtDTO.getPassword());
        newUser.setPasswordHash(hashPwd);
        UserModel savedUser = userRepository.save(newUser);

        if (registrationtDTO.getRole().equals(UserRoles.FREELANCER.toString())) {
            FreelancerModel newFreelancer = new FreelancerModel();
            newFreelancer.setFreelancerId(savedUser.getUserId());
            newFreelancer.setFirstName(registrationtDTO.getFirstName());
            newFreelancer.setLastName(registrationtDTO.getLastName());
            newFreelancer.setOnboardingCompleted(false);
            newFreelancer.setUser(savedUser);
            if (registrationtDTO.getSummary() != null)
                newFreelancer.getBio().setSummary(registrationtDTO.getSummary());
            if (registrationtDTO.getFacebookUrl() != null)
                newFreelancer.getBio().setFacebookLink(registrationtDTO.getFacebookUrl());
            if (registrationtDTO.getLinkedlnUrl() != null)
                newFreelancer.getBio().setLinkedinLink(registrationtDTO.getLinkedlnUrl());
            if (registrationtDTO.getGithubUrl() != null)
                newFreelancer.getBio().setTwitterLink(registrationtDTO.getGithubUrl());
            FreelancerModel savedFreelancer = freelancersRepository.save(newFreelancer);
            String stripeConnectAccountId = paymentService.createStripeConnectAccount(savedUser.getEmail(), "US", savedFreelancer.getFreelancerId());
            savedFreelancer.setStripeAccountId(stripeConnectAccountId);
            freelancersRepository.save(savedFreelancer);
            registrationtResponseDTO.setRole(UserRoles.FREELANCER.toString());
        }

        if (registrationtDTO.getRole().equals(UserRoles.CLIENT.toString())) {
            ClientModel newClient = new ClientModel();
            newClient.setClientId(savedUser.getUserId());
            newClient.setFirstName(registrationtDTO.getFirstName());
            newClient.setLastName(registrationtDTO.getLastName());
            if(registrationtDTO.getSummary() != null)
                newClient.getBio().setSummary(registrationtDTO.getSummary());
            if (registrationtDTO.getFacebookUrl() != null)
                newClient.getBio().setFacebookLink(registrationtDTO.getFacebookUrl());
            if (registrationtDTO.getLinkedlnUrl() != null)
                newClient.getBio().setLinkedinLink(registrationtDTO.getLinkedlnUrl());
            if (registrationtDTO.getGithubUrl() != null)
                newClient.getBio().setTwitterLink(registrationtDTO.getGithubUrl());
            newClient.setUser(savedUser);
            clientsRepository.save(newClient);
            registrationtResponseDTO.setRole(UserRoles.CLIENT.toString());
        }

        if(registrationtDTO.getRole().equals(UserRoles.ADMIN.toString())){
            AdminModel newAdmin = new AdminModel();
            newAdmin.setAdminId(savedUser.getUserId());
            newAdmin.setFirstName(registrationtDTO.getFirstName());
            newAdmin.setLastName(registrationtDTO.getLastName());
            newAdmin.setUser(savedUser);
            adminsRepository.save(newAdmin);
            registrationtResponseDTO.setRole(UserRoles.ADMIN.toString());
        }
        return registrationtResponseDTO;
    }

}
