package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.MessageDTO;
import com.freelancemarketplace.backend.mapper.MessageMapper;
import com.freelancemarketplace.backend.model.MessageModel;
import com.freelancemarketplace.backend.repository.MessagesRepository;
import com.freelancemarketplace.backend.service.MessageService;
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
