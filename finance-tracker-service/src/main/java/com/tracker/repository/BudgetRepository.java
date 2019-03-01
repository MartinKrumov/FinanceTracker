package com.tracker.repository;

import com.tracker.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Martin Krumov
 */
public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
