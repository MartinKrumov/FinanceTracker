package com.financetracker.area.category.repository;

import com.financetracker.area.category.domain.Category;
import com.financetracker.area.transaction.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndType(String name, TransactionType type);

}
