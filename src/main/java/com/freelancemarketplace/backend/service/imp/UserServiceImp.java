package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.RegistrationtDTO;
import com.freelancemarketplace.backend.enums.UserRoles;
import com.freelancemarketplace.backend.mapper.UserMapper;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.UserModel;
import com.freelancemarketplace.backend.repository.ClientsRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.UserRepository;
import com.freelancemarketplace.backend.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FreelancersRepository freelancersRepository;
    private final ClientsRepository clientsRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImp(UserRepository userRepository,
                          UserMapper userMapper,
                          FreelancersRepository freelancersRepository,
                          ClientsRepository clientsRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.freelancersRepository = freelancersRepository;
        this.clientsRepository = clientsRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public RegistrationtDTO registerUser(RegistrationtDTO registrationtDTO) {
        RegistrationtDTO registrationtResponseDTO = new RegistrationtDTO();
        UserModel newUser = userMapper.registraionDtoToUserEntity(registrationtDTO);
        String hashPwd = passwordEncoder.encode(registrationtDTO.getPassword());
        newUser.setPasswordHash(hashPwd);
        UserModel savedUser = userRepository.save(newUser);

        registrationtResponseDTO = userMapper.userEntityToRegistrationDTO(savedUser);
        registrationtResponseDTO.setUsername(registrationtResponseDTO.getUsername());
        registrationtResponseDTO.setFirstName(registrationtDTO.getFirstName());
        registrationtResponseDTO.setLastName(registrationtDTO.getLastName());


        if(registrationtDTO.getRole().equals(UserRoles.FREELANCER.toString())){
            FreelancerModel newFreelancer = new FreelancerModel();
            newFreelancer.setFreelancerId(savedUser.getUserId());
            newFreelancer.setFirstName(registrationtDTO.getFirstName());
            newFreelancer.setLastName(registrationtDTO.getLastName());
            freelancersRepository.save(newFreelancer);
            registrationtResponseDTO.setRole(UserRoles.FREELANCER.toString());
        }

        if(registrationtDTO.getRole().equals(UserRoles.CLIENT.toString())){
            ClientModel newClient = new ClientModel();
            newClient.setClientId(savedUser.getUserId());
            newClient.setFirstName(registrationtDTO.getFirstName());
            newClient.setLastName(registrationtDTO.getLastName());
            clientsRepository.save(newClient);
            registrationtResponseDTO.setRole(UserRoles.CLIENT.toString());
        }
        return registrationtResponseDTO;
    }

}
