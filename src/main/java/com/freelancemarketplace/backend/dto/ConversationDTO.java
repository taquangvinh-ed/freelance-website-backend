package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.ConversationType;
import com.freelancemarketplace.backend.model.ConversationParticipantModel;
import com.freelancemarketplace.backend.model.TeamModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ConversationDTO {

    private Long partnerId;

    private String partnerFullName;

    private String partnerAvatar;

    private String partnerRole;

    private String lastMessage;

    private Timestamp lastMessageTime;

    private int unreadCount;

    private String roomId;

}
