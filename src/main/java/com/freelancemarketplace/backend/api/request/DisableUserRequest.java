package com.freelancemarketplace.backend.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DisableUserRequest {
    String reason;
}
