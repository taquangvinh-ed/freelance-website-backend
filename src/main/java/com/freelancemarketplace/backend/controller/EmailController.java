package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/email", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void>sendEmail(@RequestParam String receiver, @RequestParam String projectTitle, @RequestParam String text){
        emailService.sendSimpleEmail(receiver, projectTitle, text);
        return ResponseEntity.ok().build();
    }

}
