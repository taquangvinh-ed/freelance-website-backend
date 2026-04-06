package com.freelancemarketplace.backend.conversation.application.service.imp;

import com.freelancemarketplace.backend.conversation.dto.MessageDTO;
import com.freelancemarketplace.backend.conversation.infrastructure.mapper.MessageMapper;
import com.freelancemarketplace.backend.conversation.domain.model.MessageModel;
import com.freelancemarketplace.backend.conversation.infrastructure.repository.MessagesRepository;
import com.freelancemarketplace.backend.conversation.application.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImp implements MessageService {

    private MessagesRepository messagesRepository;
    private MessageMapper messageMapper;


    @Override
    public List<MessageDTO> getPrivateMessages(String roomId) {
        List<MessageModel> messageModels = messagesRepository.findByRoomId(roomId);

        return messageMapper.toDTOs(messageModels);

    }


}
