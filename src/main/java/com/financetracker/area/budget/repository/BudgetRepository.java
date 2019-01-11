package com.financetracker.area.budget.repository;

import com.financetracker.area.budget.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Martin Krumov
 */
public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
