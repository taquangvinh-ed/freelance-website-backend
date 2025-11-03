package com.freelancemarketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClockifyStartRequest {
    private String start; // Timestamp bắt đầu (ISO 8601 format)
    private String description;
    private Long projectId;
    private String taskId; // Tùy chọn, nếu bạn dùng Task trong Clockify
    private boolean billable = true;
}
