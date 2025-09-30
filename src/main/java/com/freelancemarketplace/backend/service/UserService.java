package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.UserDTO;

public interface UserService {
    UserDTO chooseUsername(Long userId, String username);
    UserDTO chooseRole(Long userId, String role);
}
