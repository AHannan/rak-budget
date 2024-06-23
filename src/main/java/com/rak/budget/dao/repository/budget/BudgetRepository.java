package com.rak.budget.dao.repository.budget;

import com.rak.budget.dao.model.budget.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String>, JpaSpecificationExecutor<Budget> {
    boolean existsByUserIdAndCategoryId(String userId, String categoryId);
    Optional<Budget> findByCategoryIdAndUserId(String categoryId, String userId);

}
