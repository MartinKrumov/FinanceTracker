package com.tracker.repository;

import com.tracker.domain.Category;
import com.tracker.domain.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameAndType(String name, TransactionType type);

    Boolean existsByNameOrType(String name, TransactionType type);
}
