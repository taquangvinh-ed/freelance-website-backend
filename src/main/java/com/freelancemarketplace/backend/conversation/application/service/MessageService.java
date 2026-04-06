package com.freelancemarketplace.backend.conversation.application.service;

import com.freelancemarketplace.backend.conversation.dto.MessageDTO;

import java.util.List;

public interface MessageService {

    List<MessageDTO> getPrivateMessages(String roomId);


}
