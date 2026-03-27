package com.freelancemarketplace.backend.specification;

import com.freelancemarketplace.backend.enums.BudgetTypes;
import com.freelancemarketplace.backend.model.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProjectSpecification {

    /**
     * Lọc dự án theo: skills, budget, scope
     */
    public static Specification<ProjectModel> filter(
            List<String> skillNames,
            BigDecimal minRate,
            BigDecimal maxRate,
            Boolean isHourly,
            String duration,     // MỚI: "1 to 3 months"
            String level,        // MỚI: "Intermediate"
            String workload      // MỚI: "Part-time" (optional)
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // === 1. LỌC THEO SKILLS ===
            if (skillNames != null && !skillNames.isEmpty()) {
                Join<ProjectModel, SkillModel> skillsJoin = root.join("skills", JoinType.LEFT);
                List<String> normalizedSkillNames = skillNames.stream()
                        .filter(name -> name != null && !name.trim().isEmpty())
                        .map(name -> name.trim().toLowerCase(Locale.ROOT))
                        .collect(Collectors.toList());

                if (!normalizedSkillNames.isEmpty()) {
                    predicates.add(criteriaBuilder.lower(skillsJoin.get("name")).in(normalizedSkillNames));
                    query.distinct(true);
                }
            }

            // === 2. LỌC THEO BUDGET ===
            Join<ProjectModel, BudgetModel> budgetJoin = root.join("budget", JoinType.LEFT);

            if (isHourly != null) {
                BudgetTypes type = isHourly ? BudgetTypes.HOURLY_RATE : BudgetTypes.FIXED_PRICE;
                predicates.add(criteriaBuilder.equal(budgetJoin.get("type"), type));

                if (isHourly) {
                    if (minRate != null) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("minValue"), minRate));
                    }
                    if (maxRate != null) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("maxValue"), maxRate));
                    }
                } else {
                    if (minRate != null) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("fixedValue"), minRate));
                    }
                    if (maxRate != null) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("fixedValue"), maxRate));
                    }
                }
            } else {
                // isHourly = null → tìm cả hai loại
                if (minRate != null || maxRate != null) {
                    List<Predicate> hourly = new ArrayList<>();
                    hourly.add(criteriaBuilder.equal(budgetJoin.get("type"), BudgetTypes.HOURLY_RATE));
                    if (minRate != null) hourly.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("minValue"), minRate));
                    if (maxRate != null) hourly.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("maxValue"), maxRate));

                    List<Predicate> fixed = new ArrayList<>();
                    fixed.add(criteriaBuilder.equal(budgetJoin.get("type"), BudgetTypes.FIXED_PRICE));
                    if (minRate != null) fixed.add(criteriaBuilder.greaterThanOrEqualTo(budgetJoin.get("fixedValue"), minRate));
                    if (maxRate != null) fixed.add(criteriaBuilder.lessThanOrEqualTo(budgetJoin.get("fixedValue"), maxRate));

                    Predicate hourlyPredicate = criteriaBuilder.and(hourly.toArray(new Predicate[0]));
                    Predicate fixedPredicate = criteriaBuilder.and(fixed.toArray(new Predicate[0]));
                    predicates.add(criteriaBuilder.or(hourlyPredicate, fixedPredicate));
                }
            }

            // === 3. LỌC THEO SCOPE (MỚI) ===
            if (duration != null || level != null || workload != null) {
                // Join vào @Embedded ProjectScope
                // Vì là @Embedded → không cần Join, truy cập trực tiếp
                if (duration != null && !duration.trim().isEmpty()) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("scope").get("duration")),
                            duration.trim().toLowerCase()
                    ));
                }
                if (level != null && !level.trim().isEmpty()) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("scope").get("level")),
                            level.trim().toLowerCase()
                    ));
                }
                if (workload != null && !workload.trim().isEmpty()) {
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("scope").get("workload")),
                            workload.trim().toLowerCase()
                    ));
                }
            }

            // === KẾT HỢP TẤT CẢ ===
            return predicates.isEmpty()
                    ? criteriaBuilder.conjunction()
                    : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // === AUTOCOMPLETE (giữ nguyên) ===
    public static Specification<ProjectModel> autocompleteSearch(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.disjunction();
            }

            String pattern = "%" + keyword.trim().toLowerCase(Locale.ROOT) + "%";
            Predicate title = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern);
            Predicate desc = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern);

            return criteriaBuilder.or(title, desc);
        };
    }
}