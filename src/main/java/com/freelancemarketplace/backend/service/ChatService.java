package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.MessageDTO;
import com.freelancemarketplace.backend.dto.ContactInfoDTO;

public interface ChatService {

    public MessageDTO saveMessageToDatabase(Long senderId, MessageDTO messageDTO);


    ContactInfoDTO getContactInfo(Long userId);
}
