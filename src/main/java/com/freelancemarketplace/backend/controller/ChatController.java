package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.ContactInfoDTO;
import com.freelancemarketplace.backend.dto.ConversationDTO;
import com.freelancemarketplace.backend.dto.CurrentUserProfileDTO;
import com.freelancemarketplace.backend.dto.MessageDTO;
import com.freelancemarketplace.backend.model.MessageModel;
import com.freelancemarketplace.backend.model.UserModel;
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

import java.util.List;

@RestController
@AllArgsConstructor
public class ChatController {

    private SimpMessagingTemplate messagingTemplate;
    private ChatService chatService;

    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload MessageDTO messageDTO){


        String roomId = chatService.createRoomId(messageDTO.getSenderId(), messageDTO.getReceiverId());

        MessageDTO response = chatService.saveMessageToDatabase(roomId, messageDTO);

        messagingTemplate.convertAndSend("/topic/chat/" + roomId,  response);
    }


    @GetMapping("/api/chat/getContactInfo/{userId}")
    ResponseEntity<ContactInfoDTO> getContactInfo(@PathVariable Long userId){
        ContactInfoDTO contactInfo = chatService.getContactInfo(userId);
        return ResponseEntity.ok(contactInfo);
    }

    @GetMapping("/api/chat/getRecentConversation/")
    ResponseEntity<List<ConversationDTO>> getRecentConversation(@AuthenticationPrincipal AppUser appUser){
        if(appUser == null){
            throw  new SecurityException("User not authenticated");
        }
        Long userId = appUser.getId();
        List<ConversationDTO> conversations = chatService.getRecentConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/api/messages/history/user1Id/{user1Id}/user2Id/{user2Id}")
    ResponseEntity<List<MessageDTO>> fetchMessagesHistory(@PathVariable Long user1Id, @PathVariable Long user2Id){
        List<MessageDTO> messages = chatService.fetchMessageHistory(user1Id, user2Id);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/api/chat/currentUserProfile")
    ResponseEntity<CurrentUserProfileDTO> getCurrentUserProfile(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        CurrentUserProfileDTO userProfile = chatService.getCurrentUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

}
