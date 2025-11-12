package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.request.SendEmailRequest;
import com.freelancemarketplace.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/email", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-invitation")
    public ResponseEntity<Void>sendEmail(@AuthenticationPrincipal AppUser appUser, @RequestBody SendEmailRequest request){
        Long clientId = appUser.getId();
        emailService.sendSimpleEmail(clientId, request.getFreelancerId(), request.getProjectId(),request.getReceiver(), request.getProjectTitle(), request.getText());
        return ResponseEntity.ok().build();
    }

}
