package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

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
