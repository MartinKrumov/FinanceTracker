package com.tracker.repository;

import com.tracker.domain.Category;
import com.tracker.domain.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByNameAndType(String name, TransactionType type);
}
