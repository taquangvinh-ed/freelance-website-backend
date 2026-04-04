package com.freelancemarketplace.backend.conversation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactInfoDTO {

    private Long userId;

    private String firstName;

    private String lastName;

    private String avatar;

    private String lastMessage;

    private String role;

}
