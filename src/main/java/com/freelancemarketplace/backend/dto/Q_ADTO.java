package com.freelancemarketplace.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Q_ADTO {
    private Long qaId;

    private String question;

    private String answer;

    private String tag;

    @NotNull(message = "id of admin inside DTO must not be null")
    private Long adminId;

    public Q_ADTO(Q_ADTO qAdto) {
    }
}
