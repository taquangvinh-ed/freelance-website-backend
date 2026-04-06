package com.freelancemarketplace.backend.user.application.service.imp;

import com.freelancemarketplace.backend.user.dto.RegistrationtDTO;
import com.freelancemarketplace.backend.user.domain.enums.AccountStatus;
import com.freelancemarketplace.backend.user.domain.enums.UserRoles;
import com.freelancemarketplace.backend.user.infrastructure.mapper.UserMapper;
import com.freelancemarketplace.backend.admin.domain.model.AdminModel;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.user.domain.model.UserModel;
import com.freelancemarketplace.backend.admin.infrastructure.repository.AdminsRepository;
import com.freelancemarketplace.backend.client.infrastructure.repository.ClientsRepository;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.user.infrastructure.repository.UserRepository;
import com.freelancemarketplace.backend.payment.application.service.PaymentService;
import com.freelancemarketplace.backend.user.application.service.UserService;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

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
        newUser.setAccountStatus(AccountStatus.ACTIVE);
        newUser.setFullName(registrationtDTO.getFirstName() + " " + registrationtDTO.getLastName());
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


    @Override
    public long countAllUserByRole(UserRoles role){
        return userRepository.countByRole(role);
    }

    @Override
    public long getNewFreelancerCountWeekly(){
        LocalDateTime startOfWeek = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return userRepository.countByRoleAndCreatedAtAfter(UserRoles.FREELANCER, startOfWeek);
    }

    @Override
    public long getNewFreelancerCountMonthly() {
        // Lấy ngày đầu tiên của tháng hiện tại
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        return userRepository.countByRoleAndCreatedAtAfter(UserRoles.FREELANCER, startOfMonth);
    }

    @Override
    public long getNewClientCountWeekly(){
        LocalDateTime startOfWeek = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return userRepository.countByRoleAndCreatedAtAfter(UserRoles.CLIENT, startOfWeek);
    }

    @Override
    public long getNewClientCountMonthly() {
        // Lấy ngày đầu tiên của tháng hiện tại
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        return userRepository.countByRoleAndCreatedAtAfter(UserRoles.CLIENT, startOfMonth);
    }

}
