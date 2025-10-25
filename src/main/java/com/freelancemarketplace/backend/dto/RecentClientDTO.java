package com.freelancemarketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class RecentClientDTO {
    Long clientId;
    String avatar;
    String clientName;
    String projectName;
    Timestamp lastWorked;

}
