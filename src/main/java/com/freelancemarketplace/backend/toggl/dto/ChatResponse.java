package com.freelancemarketplace.backend.toggl.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private boolean success;
    private String message;
    private String timestamp;
    private Body body;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        private String messageId;
        private String aiResponse;
        private ResponseType responseType;
        private SuggestionUpdate suggestionUpdate;
        private List<String> tips;
        private boolean requiresFollowUp;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuggestionUpdate {
        private String field;
        private String originalValue;
        private String suggestedValue;
        private String reason;
        private Double confidence;
    }

    public enum ResponseType {
        INFO,
        SUGGESTION,
        TIP,
        QUESTION,
        WARNING,
        UPDATE
    }
}
