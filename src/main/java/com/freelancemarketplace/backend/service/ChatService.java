package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ConversationDTO;
import com.freelancemarketplace.backend.dto.CurrentUserProfileDTO;
import com.freelancemarketplace.backend.dto.MessageDTO;
import com.freelancemarketplace.backend.dto.ContactInfoDTO;

import java.util.List;

public interface ChatService {

    public MessageDTO saveMessageToDatabase(String roomId, MessageDTO messageDTO);


    ContactInfoDTO getContactInfo(Long userId);

    List<ConversationDTO> getRecentConversations(Long userId);

    String createRoomId(Long userId1, Long userId2);

    List<MessageDTO> fetchMessageHistory(Long senderId, Long receiverId);

    CurrentUserProfileDTO getCurrentUserProfile(Long userId);
}
