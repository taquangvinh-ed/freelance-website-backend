package com.freelancemarketplace.backend.recommendation.application.service;

import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScoredFreelancer {
    private  FreelancerModel freelancer;
    private  double score;
}
