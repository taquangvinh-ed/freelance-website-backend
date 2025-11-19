package com.freelancemarketplace.backend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DisableUserRequest {
    String reason;
}
