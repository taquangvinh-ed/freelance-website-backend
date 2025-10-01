package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);
    UserDTO chooseUsername(Long userId, String username);
    UserDTO chooseRole(Long userId, String role, String firstName, String lastName);
}
