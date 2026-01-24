package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Messages",
indexes ={
        @Index(name = "idx_room_sentAt", columnList = "roomId, sentAt DESC"),
        @Index(name = "idx_receiver_isRead", columnList = "receiverId, isRead" )
        })
public class MessageModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    private  Long senderId;
    private Long receiverId;
    private String roomId;
    private String type;
    private String fileName;
    private String content;
    private Timestamp sentAt;
    private Boolean isRead = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;








}
