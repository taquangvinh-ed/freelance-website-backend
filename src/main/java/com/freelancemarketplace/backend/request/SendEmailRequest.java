package com.freelancemarketplace.backend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    private String receiver;
    private String projectTitle;
    private String text;
    private Long  freelancerId;
    private Long projectId;
}
