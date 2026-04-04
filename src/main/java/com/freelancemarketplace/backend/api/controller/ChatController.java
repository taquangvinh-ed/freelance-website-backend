package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.dto.ContactInfoDTO;
import com.freelancemarketplace.backend.dto.ConversationDTO;
import com.freelancemarketplace.backend.dto.CurrentUserProfileDTO;
import com.freelancemarketplace.backend.dto.MessageDTO;
import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ApiResponse<?> getContactInfo(@PathVariable Long userId){
        ContactInfoDTO contactInfo = chatService.getContactInfo(userId);
        return ApiResponse.success(contactInfo);
    }

    @GetMapping("/api/chat/getRecentConversation/")
    public ApiResponse<?> getRecentConversation(@AuthenticationPrincipal AppUser appUser){
        if(appUser == null){
            throw  new SecurityException("User not authenticated");
        }
        Long userId = appUser.getId();
        List<ConversationDTO> conversations = chatService.getRecentConversations(userId);
        return ApiResponse.success(conversations);
    }

    @GetMapping("/api/messages/history/user1Id/{user1Id}/user2Id/{user2Id}")
    public ApiResponse<?> fetchMessagesHistory(@PathVariable Long user1Id, @PathVariable Long user2Id){
        List<MessageDTO> messages = chatService.fetchMessageHistory(user1Id, user2Id);
        return ApiResponse.success(messages);
    }

    @GetMapping("/api/chat/currentUserProfile")
    public ApiResponse<?> getCurrentUserProfile(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        CurrentUserProfileDTO userProfile = chatService.getCurrentUserProfile(userId);
        return ApiResponse.success(userProfile);
    }

    @PatchMapping("/api/message/mark-as-read/{partnerId}")
    public ApiResponse<?> updateMessageIsRead(@AuthenticationPrincipal AppUser appUser, @PathVariable Long partnerId){
        Long userId = appUser.getId();
        chatService.markAsRead(partnerId, userId);
        return ApiResponse.success("Messages marked as read");
    }

}
