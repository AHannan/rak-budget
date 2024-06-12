package com.rak.budget.dao.repository.budget;

import com.rak.budget.dao.model.budget.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, String> {
}
