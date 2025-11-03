package com.freelancemarketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClockifyTimeEntryResponse {
    private String id;
    private String description;
    private String projectId;
    private TimeInterval timeInterval;


    @Getter
    @Setter
    public static class TimeInterval {
        private String start; // Timestamp bắt đầu
        private String end;   // Timestamp kết thúc (có thể là null)
        private String duration; // Thời lượng

    }
}
