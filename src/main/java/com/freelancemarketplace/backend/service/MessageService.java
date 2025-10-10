package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.MessageDTO;

import java.util.List;

public interface MessageService {

    List<MessageDTO> getPrivateMessages(Long roomId);

    List<MessageDTO> getTeamMessages(Long teamId);

}
