package com.freelancemarketplace.backend.specification;

import com.freelancemarketplace.backend.enums.BudgetTypes;
import com.freelancemarketplace.backend.enums.ProjectStatus;
import com.freelancemarketplace.backend.model.BudgetModel;
import com.freelancemarketplace.backend.model.CategoryModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.SkillModel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProjectSpecification {

    public static Specification<ProjectModel> advancedSearch(
            String keyword, Long categoryId, List<String> skillNames,
            Double minRate, Double maxRate, Boolean isHourly, String status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Tìm kiếm theo keyword trong title và description
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.trim().toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), likePattern);
                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), likePattern);
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate));
            }

            // Lọc theo categoryId
            if (categoryId != null) {
                Join<ProjectModel, CategoryModel> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), categoryId));
            }

            // Lọc theo skillNames
            if (skillNames != null && !skillNames.isEmpty()) {
                Join<ProjectModel, SkillModel> skillsJoin = root.join("skills");
                predicates.add(skillsJoin.get("name").in(skillNames));
            }

            // Lọc theo status
            if (status != null && !status.isEmpty()) {
                try {
                    ProjectStatus projectStatus = ProjectStatus.valueOf(status);
                    predicates.add(criteriaBuilder.equal(root.get("status"), projectStatus));
                } catch (IllegalArgumentException e) {
                    // Nếu status không hợp lệ, bỏ qua hoặc xử lý tùy thuộc vào yêu cầu
                    predicates.add(criteriaBuilder.isNull(root.get("status")));
                }
            }

            // Lọc theo ngân sách (minRate, maxRate, isHourly)
            if (isHourly != null) {
                Join<ProjectModel, BudgetModel> budgetJoin = root.join("budget");
                if (isHourly) {
                    // Lọc theo hourly rate
                    predicates.add(criteriaBuilder.equal(budgetJoin.get("type"), BudgetTypes.HOURLY_RATE));
                    if (minRate != null) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("minValue"), minRate));
                    }
                    if (maxRate != null) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("maxValue"), maxRate));
                    }
                } else {
                    // Lọc theo fixed price
                    predicates.add(criteriaBuilder.equal(budgetJoin.get("type"), BudgetTypes.FIXED_PRICE));
                    if (minRate != null) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("fixedValue"), minRate));
                    }
                    if (maxRate != null) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("fixedValue"), maxRate));
                    }
                }
            }

            // Kết hợp tất cả các điều kiện
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
