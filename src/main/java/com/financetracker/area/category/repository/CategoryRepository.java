package com.financetracker.area.category.repository;

import com.financetracker.area.category.domain.Category;
import com.financetracker.area.transaction_type.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByNameAndType(String name, TransactionType type);
}
