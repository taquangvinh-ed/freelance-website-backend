package com.freelancemarketplace.backend.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class UserRequest {
    int page;
    int size;
    String query;
    String status;
    String role;
}
