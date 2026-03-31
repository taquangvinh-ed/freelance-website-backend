package com.freelancemarketplace.backend.recommandation;

import com.freelancemarketplace.backend.domain.model.FreelancerModel;
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
