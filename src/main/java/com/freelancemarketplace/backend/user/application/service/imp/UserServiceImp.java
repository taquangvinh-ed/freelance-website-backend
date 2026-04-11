package com.freelancemarketplace.backend.user.application.service.imp;

import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.user.dto.RegistrationtDTO;
import com.freelancemarketplace.backend.user.domain.enums.AccountStatus;
import com.freelancemarketplace.backend.user.domain.enums.UserRoles;
import com.freelancemarketplace.backend.user.infrastructure.mapper.UserMapper;
import com.freelancemarketplace.backend.user.domain.model.UserModel;
import com.freelancemarketplace.backend.user.infrastructure.repository.UserRepository;
import com.freelancemarketplace.backend.user.application.service.UserService;
import com.freelancemarketplace.backend.user.application.service.UserFactory;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, UserFactory<? extends BaseEntity>> userFactories;

    @Override
    @Transactional
    public RegistrationtDTO registerUser(RegistrationtDTO registrationtDTO) throws Exception {
        UserModel newUser = userMapper.registraionDtoToUserEntity(registrationtDTO);
        String hashPwd = passwordEncoder.encode(registrationtDTO.getPassword());
        newUser.setPasswordHash(hashPwd);
        newUser.setAccountStatus(AccountStatus.ACTIVE);
        newUser.setFullName(registrationtDTO.getFirstName() + " " + registrationtDTO.getLastName());
        UserModel savedUser = userRepository.save(newUser);

        String factoryKey = getFactoryKey(registrationtDTO.getRole());
        UserFactory<? extends BaseEntity> factory = userFactories.get(factoryKey);
        
        if (factory != null) {
            factory.createProfile(registrationtDTO, savedUser);
        }

        RegistrationtDTO response = new RegistrationtDTO();
        response.setRole(registrationtDTO.getRole());
        return response;
    }

    private String getFactoryKey(String role) {
        return switch (role.toUpperCase()) {
            case "FREELANCER" -> "freelancerFactory";
            case "CLIENT" -> "clientFactory";
            case "ADMIN" -> "adminFactory";
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    @Override
    public long countAllUserByRole(UserRoles role) {
        return userRepository.countByRole(role);
    }

    @Override
    public long getNewFreelancerCountWeekly() {
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
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        return userRepository.countByRoleAndCreatedAtAfter(UserRoles.FREELANCER, startOfMonth);
    }

    @Override
    public long getNewClientCountWeekly() {
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
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        return userRepository.countByRoleAndCreatedAtAfter(UserRoles.CLIENT, startOfMonth);
    }
}
