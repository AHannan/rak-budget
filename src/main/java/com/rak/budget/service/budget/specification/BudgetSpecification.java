package com.rak.budget.service.budget.specification;

import com.rak.budget.dao.model.budget.Budget;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class BudgetSpecification {

    public static Specification<Budget> filterByUserId(String userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<Budget> filterByCategoryId(String categoryId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Budget> buildSpecification(String userId, String categoryId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(filterByUserId(userId).toPredicate(root, query, criteriaBuilder));
            }
            if (categoryId != null) {
                predicates.add(filterByCategoryId(categoryId).toPredicate(root, query, criteriaBuilder));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
