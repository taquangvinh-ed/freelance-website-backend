package com.freelancemarketplace.backend.specification;

import com.freelancemarketplace.backend.model.SkillModel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SkillSpecification {

    public static Specification<SkillModel> searchByName(String skillName){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (skillName != null && !skillName.trim().isEmpty()) {
                String likePattern = "%" + skillName.trim().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        likePattern
                );
                predicates.add(namePredicate);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
