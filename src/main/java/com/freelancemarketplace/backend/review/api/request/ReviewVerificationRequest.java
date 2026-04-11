package com.freelancemarketplace.backend.review.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewVerificationRequest {
    private Boolean approved;
    private String rejectionReason;
}

