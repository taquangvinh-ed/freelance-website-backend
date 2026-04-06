package com.freelancemarketplace.backend.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentResponse {
    String paymentIntentId;
    String clientSecret;
}
