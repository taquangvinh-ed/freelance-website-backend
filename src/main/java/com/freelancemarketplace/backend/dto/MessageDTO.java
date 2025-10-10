package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class MessageDTO {

    private Long messageId;

    private Long senderId;
    private Long receiverId;
    private String roomId;
    private String type;
    private String fileName;
    private String content;
    private Timestamp sentAt;
}
