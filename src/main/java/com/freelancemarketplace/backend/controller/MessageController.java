package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.MessageDTO;
import com.freelancemarketplace.backend.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;


    @GetMapping("/private/{roomId}")
    ResponseEntity<List<MessageDTO>> getPrivateMessages(@PathVariable Long roomId){
        List<MessageDTO> messages = messageService.getPrivateMessages(roomId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/private/{teamId}")
    ResponseEntity<List<MessageDTO>> getTeamMessages(@PathVariable Long teamId){
        List<MessageDTO> messages = messageService.getTeamMessages(teamId);
        return ResponseEntity.ok(messages);
    }


}
