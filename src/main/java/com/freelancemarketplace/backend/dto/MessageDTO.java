package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor
public class MessageDTO {

    private Long messageId;

    private Long senderId;
    private Long receiverId;
    private Long roomId;
    private Long teamId;
    private String type;
    private String fileName;
    private String content;
    private Timestamp sentAt;
}
