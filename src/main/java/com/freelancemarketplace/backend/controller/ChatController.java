package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.ContactInfoDTO;
import com.freelancemarketplace.backend.dto.MessageDTO;
import com.freelancemarketplace.backend.model.MessageModel;
import com.freelancemarketplace.backend.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ChatController {

    private SimpMessagingTemplate messagingTemplate;
    private ChatService chatService;

    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@AuthenticationPrincipal AppUser appUser, @Payload MessageDTO messageDTO){

        if(appUser == null){
            throw  new SecurityException("User not authenticated");
        }
        Long senderId = appUser.getId();

        MessageDTO response = chatService.saveMessageToDatabase(senderId, messageDTO);

        Long roomId = response.getRoomId();

        messagingTemplate.convertAndSend("/topic/chat/" + roomId,  response);
    }


    @GetMapping("/api/chat/getContactInfo/{userId}")
    ResponseEntity<ContactInfoDTO> getContactInfo(@PathVariable Long userId){
        ContactInfoDTO contactInfo = chatService.getContactInfo(userId);
        return ResponseEntity.ok(contactInfo);
    }

}
