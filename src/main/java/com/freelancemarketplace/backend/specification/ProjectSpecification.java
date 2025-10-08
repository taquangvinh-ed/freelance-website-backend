package com.freelancemarketplace.backend.specification;

import com.freelancemarketplace.backend.enums.BudgetTypes;
import com.freelancemarketplace.backend.enums.ProjectStatus;
import com.freelancemarketplace.backend.model.BudgetModel;
import com.freelancemarketplace.backend.model.CategoryModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.SkillModel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProjectSpecification {

    public static Specification<ProjectModel> filter(
            List<String> skillNames,
            BigDecimal minRate,
            BigDecimal maxRate,
            Boolean isHourly) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo skillNames
            if (skillNames != null && !skillNames.isEmpty()) {
                Join<ProjectModel, SkillModel> skillsJoin = root.join("skills", JoinType.LEFT);
                predicates.add(skillsJoin.get("name").in(skillNames));
            }

            // Lọc theo ngân sách (minRate, maxRate, isHourly)
            Join<ProjectModel, BudgetModel> budgetJoin = root.join("budget", JoinType.LEFT);
            if (isHourly != null) {
                if (isHourly) {
                    predicates.add(criteriaBuilder.equal(budgetJoin.get("type"), BudgetTypes.HOURLY_RATE));
                    if (minRate != null && maxRate != null && minRate.compareTo(maxRate) > 0) {
                        throw new IllegalArgumentException("minRate must be less than or equal to maxRate for Hourly Rate");
                    }
                    if (minRate != null) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("minValue"), minRate));
                    }
                    if (maxRate != null) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("maxValue"), maxRate));
                    }
                } else {
                    predicates.add(criteriaBuilder.equal(budgetJoin.get("type"), BudgetTypes.FIXED_PRICE));
                    if (minRate != null && maxRate != null && minRate.compareTo(maxRate) > 0) {
                        throw new IllegalArgumentException("minRate must be less than or equal to maxRate for Fixed Price");
                    }
                    if (minRate != null) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("fixedValue"), minRate));
                    }
                    if (maxRate != null) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("fixedValue"), maxRate));
                    }
                }
            } else {
                // Trường hợp "All" (isHourly = null)
                if (minRate != null || maxRate != null) {
                    // Xây dựng điều kiện cho Hourly Rate
                    List<Predicate> hourlyPredicates = new ArrayList<>();
                    hourlyPredicates.add(criteriaBuilder.equal(budgetJoin.get("type"), BudgetTypes.HOURLY_RATE));
                    if (minRate != null) {
                        hourlyPredicates.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("minValue"), minRate));
                    }
                    if (maxRate != null) {
                        hourlyPredicates.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("maxValue"), maxRate));
                    }
                    if (!hourlyPredicates.isEmpty()) {
                        predicates.add(criteriaBuilder.and(hourlyPredicates.toArray(new Predicate[0])));
                    }

                    // Xây dựng điều kiện cho Fixed Price
                    List<Predicate> fixedPredicates = new ArrayList<>();
                    fixedPredicates.add(criteriaBuilder.equal(budgetJoin.get("type"), BudgetTypes.FIXED_PRICE));
                    if (minRate != null) {
                        fixedPredicates.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("fixedValue"), minRate));
                    }
                    if (maxRate != null) {
                        fixedPredicates.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("fixedValue"), maxRate));
                    }
                    if (!fixedPredicates.isEmpty()) {
                        predicates.add(criteriaBuilder.and(fixedPredicates.toArray(new Predicate[0])));
                    }

                    // Nếu cả minRate và maxRate đều null, không thêm điều kiện ngân sách
                }
            }

            // Kết hợp tất cả các điều kiện
            return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ProjectModel> autocompleteSearch(String keyword, int limit) {
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

            // Sắp xếp theo title để đảm bảo kết quả nhất quán
            query.orderBy(criteriaBuilder.asc(root.get("title")));

            // Kết hợp các điều kiện
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
