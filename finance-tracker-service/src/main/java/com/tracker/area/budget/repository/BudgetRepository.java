package com.tracker.area.budget.repository;

import com.tracker.area.budget.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Martin Krumov
 */
public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
