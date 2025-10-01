package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.UserDTO;
import com.freelancemarketplace.backend.enums.UserRoles;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.exception.UserException;
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
    public UserDTO createUser(UserDTO userDTO) {
        UserModel newUser = userMapper.toEntity(userDTO);
        String hashPwd = passwordEncoder.encode(userDTO.getPassword());
        newUser.setPasswordHash(hashPwd);
        UserModel savedUser = userRepository.save(newUser);


        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDTO chooseUsername(Long userId, String username) {
        UserModel user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with id: " + userId + " not found")
        );
        if(userRepository.existsByUsername(username))
            throw new UserException("username has alreadyExist");

        user.setUsername(username);
        UserModel savedUser = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDTO chooseRole(Long userId, String role, String firstName, String lastName) {
        UserModel user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with id: " + userId + " not found")
        );

        user.setRole(UserRoles.valueOf(role));
        UserModel savedUser = userRepository.save(user);

        if(role.equals(UserRoles.FREELANCER.toString())){
            FreelancerModel newFreelancer = new FreelancerModel();
            newFreelancer.setFreelancerId(savedUser.getUserId());
            newFreelancer.setFirstName(firstName);
            newFreelancer.setLastName(lastName);
            freelancersRepository.save(newFreelancer);
        }

        if(role.equals(UserRoles.CLIENT.toString())){
            ClientModel newClient = new ClientModel();
            newClient.setClientId(savedUser.getUserId());
            newClient.setFirstName(firstName);
            newClient.setLastName(lastName);
            clientsRepository.save(newClient);
        }

        return userMapper.toDto(user);
    }
}
