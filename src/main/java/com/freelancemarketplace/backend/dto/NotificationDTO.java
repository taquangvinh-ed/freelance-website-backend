package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    private Long notificationId;

    private String title;

    private String description;

    private Timestamp datetime;

    private String imageUrl;

    private Long adminId;


}
