package com.freelancemarketplace.backend.api.controller;
import com.freelancemarketplace.backend.api.response.ApiResponse;

import com.freelancemarketplace.backend.user.dto.RegistrationtDTO;
import com.freelancemarketplace.backend.api.response.ResponseDTO;
import com.freelancemarketplace.backend.infrastructure.security.jwt.JwtTokenProvider;
import com.freelancemarketplace.backend.infrastructure.mapper.UserMapper;
import com.freelancemarketplace.backend.domain.model.UserModel;
import com.freelancemarketplace.backend.api.response.ResponseMessage;
import com.freelancemarketplace.backend.api.response.ResponseStatusCode;
import com.freelancemarketplace.backend.user.application.service.UserService;
import com.stripe.exception.StripeException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/users", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, UserMapper userMapper) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    @PostMapping("/")
    ApiResponse<?> registerUser(@RequestBody RegistrationtDTO registrationtDTO) throws StripeException {
        RegistrationtDTO responseRegistration = userService.registerUser(registrationtDTO);

        UserModel newUser = userMapper.registraionDtoToUserEntity(registrationtDTO);
        String jwt = jwtTokenProvider.genarateToken(newUser);
        System.out.println("Jwt: " + jwt);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                newUser, null, newUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        responseRegistration.setToken(jwt);
        return ApiResponse.created(responseRegistration);
    }

}
