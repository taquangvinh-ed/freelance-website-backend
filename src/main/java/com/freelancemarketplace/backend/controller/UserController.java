package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.dto.UserDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;

@Controller
@RequestMapping(path = "/api/user", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {

    private  final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/username/{userId}")
    ResponseEntity<ResponseDTO>chooseUsername(@PathVariable Long userId, @RequestParam String username){
        UserDTO user = userService.chooseUsername(userId, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        user));
    }

    @PostMapping("/role/{userId}")
    ResponseEntity<ResponseDTO>chooseRole(@PathVariable Long userId, @RequestParam String role){
        UserDTO user = userService.chooseRole(userId, role);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        user));
    }
}
