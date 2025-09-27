package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.ConversationType;
import com.freelancemarketplace.backend.model.ConversationParticipantModel;
import com.freelancemarketplace.backend.model.TeamModel;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class ConversationDTO {

    private Long conversationId;

    private String type; //ONE_TO_ONE, GROUP

    private List<ConversationParticipantDTO> participants = new ArrayList<>();

    private Long teamId;

}
