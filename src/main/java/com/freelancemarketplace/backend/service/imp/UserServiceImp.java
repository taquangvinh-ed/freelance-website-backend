package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.UserDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.exception.UserException;
import com.freelancemarketplace.backend.mapper.UserMapper;
import com.freelancemarketplace.backend.model.UserModel;
import com.freelancemarketplace.backend.repository.UserRepository;
import com.freelancemarketplace.backend.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImp(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
    public UserDTO chooseRole(Long userId, String role) {
        UserModel user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with id: " + userId + " not found")
        );

        user.setUsername(role);
        UserModel savedUser = userRepository.save(user);
        return userMapper.toDto(user);    }
}
