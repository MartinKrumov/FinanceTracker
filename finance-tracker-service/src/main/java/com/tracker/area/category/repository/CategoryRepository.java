package com.tracker.area.category.repository;

import com.tracker.area.category.domain.Category;
import com.tracker.area.transaction.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndType(String name, TransactionType type);

}